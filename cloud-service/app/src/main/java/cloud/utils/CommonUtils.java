package cloud.utils;

import java.util.Random;

public class CommonUtils {
    private static final Random R = new Random();

    public static String generateRandomIp() {
        return R.nextInt(256) + "." + R.nextInt(256) + "." + R.nextInt(256) + "." + R.nextInt(256);

    }
}
