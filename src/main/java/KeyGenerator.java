import java.security.SecureRandom;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KeyGenerator {
    public static void generateKeyFile(String directory, int index) throws Exception {
        byte[] key = new byte[16]; // 128-bit key
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        Path keyPath = Paths.get(directory, "enc_" + index + ".key");
        Files.write(keyPath, key);
    }

    public static void createKeyInfoFile(String keyUrl, String keyPath, String iv, int index) throws Exception {
        String keyInfoContent = String.format("%s/enc_%d.key\n%senc_%d.key\n%s", keyUrl, index, keyPath, index, iv);
        Path keyInfoPath = Paths.get(keyPath, "key_info_" + index + ".txt");
        Files.write(keyInfoPath, keyInfoContent.getBytes());
    }
}
