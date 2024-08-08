import com.github.kokorin.jaffree.ffmpeg.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class MediaProcessor {
    private static final Path path = Paths.get("C:\\ProgramData\\chocolatey\\bin");
    private static final String MATERIALS = "C:\\Users\\Azim\\Desktop\\Projects\\Materials\\";

    public static void main (String [] args) throws Exception {
        httpLiveStream(MATERIALS+"video.mp4");
    }

    public static AtomicLong identifyMediaDuration (String input) {
        final AtomicLong durationMillis = new AtomicLong();

        FFmpegResult ffmpegResult = FFmpeg.atPath(path)
                .addInput(
                        UrlInput.fromUrl(input)
                )
                .addOutput(new NullOutput())
                .setProgressListener(new ProgressListener() {
                    @Override
                    public void onProgress(FFmpegProgress progress) {
                        durationMillis.set(progress.getTime());
                    }
                })
                .execute();
        return durationMillis;
    }

    public static void changeResolution (String input, String output) {
        FFmpeg.atPath(path)
                .addInput(
                        UrlInput.fromUrl(input)
                )
                .addOutput(
                        UrlOutput
                                .toUrl(output)
                                .addOption("-vf", "scale=640:480")
                )
                .execute();
    }

    public static void changeFormat(String input, String output) {
        FFmpeg.atPath(path)
                .addInput(
                        UrlInput.fromUrl(input)
                )
                .addOutput(
                        UrlOutput
                                .toUrl(output)
                )
                .execute();
    }

    public static void httpLiveStream (String input) throws Exception {
        for (int i = 0; i < 3; i++) {
            KeyGenerator.generateKeyFile(MATERIALS, i);
            KeyGenerator.createKeyInfoFile("http://your-server-ip/keys", MATERIALS, "0x" + Integer.toHexString(i), i);

            FFmpeg.atPath(path)
                    .addInput(
                            UrlInput.fromUrl(input)
                    )
                    .addOutput(
                            UrlOutput.toPath(Paths.get(MATERIALS).resolve("index_" + i + ".m3u8"))
                                    .setFormat("hls")
                                    .addOption("-hls_list_size", "3")
                                    .addOption("-hls_time", "2")
                                    .addOption("-hls_segment_filename", MATERIALS + "segment_%03d.ts")
                                    .addOption("-hls_key_info_file", MATERIALS + "key_info_" + i + ".txt")
                    )
                    .setOverwriteOutput(true)
                    .execute();
        }
    }
}
