import com.github.kokorin.jaffree.ffmpeg.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class MediaProcessorImpl {
    private static final Path path = Paths.get("C:\\ProgramData\\chocolatey\\bin");
    private static final String MATERIALS = "C:\\Users\\Azim\\Desktop\\Projects\\Materials\\";

    public static void main (String [] args) {
        //System.out.println(identifyMediaDuration(MATERIALS+"video.mp4"));
        //changeResolution(MATERIALS+"video.mp4", MATERIALS+"video.mp4"+"output.mp4");
        changeFormat(MATERIALS+"video.mp4", MATERIALS+"output.avi");
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
}
