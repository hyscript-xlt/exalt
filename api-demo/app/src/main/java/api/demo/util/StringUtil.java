package api.demo.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtil {
    public static String stringToJson(String key, String value) {
        if (key == null || value ==null) {
            throw new NullPointerException("Key and value should not be null");
        }
        return String.format("{\"%s\":\"%s\"}",key, value);
    }

    public static String stringToJsonArray(String ... args) {
        if (args.length < 2 || args.length % 2 != 0) {
            throw new IllegalArgumentException("Array length should be even");
        }

        return"[" + IntStream.iterate(0, l -> l + 2)
                .limit(args.length - 2)
                .mapToObj(i -> stringToJson(args[i], args[i + 1]))
                .collect(Collectors.joining(","))
                + "]";
    }
}
