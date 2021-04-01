package hotel.booking.service.impl;

import com.aerospike.client.Bin;
import com.aerospike.client.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T recordTo(Record record, Class<T> type) {
        return Optional.ofNullable(record)
                .map(rec -> rec.bins)
                .map(bins -> mapper.convertValue(bins, type))
                .orElse(null);
    }

    /**
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Bin[] classToBin(T clazz) {
        Method[] declaredMethods = clazz.getClass().getDeclaredMethods();
        Map<String, Method> methods = filterMethodsPartialName("get", declaredMethods);
        return methods.values()
                .stream()
                .map(e -> new Tuple2<>(extractName("get", e.getName()),
                        Try.of(() -> e.invoke(clazz)).getOrNull()))
                .filter(e -> e._2 != null)
                .map(pair -> new Bin(pair._1, pair._2))
                .toArray(Bin[]::new);
    }

    public static String extractName(String prefix, String name) {
        String processed = name.replace(prefix,"");
        return Character.toLowerCase(processed.charAt(0))  + processed.substring(1);
    }

    static <T> void updateExisting(T existing, T update) {
        Method[] declaredMethods = existing.getClass().getDeclaredMethods();
        Map<String, Method> setters = filterMethodsPartialName("set", declaredMethods);

        Arrays.stream(declaredMethods)
                .filter(e -> e.getName().contains("get") && !e.getName().equals("getId"))
                //get fields to update existing
                .filter(getter -> Try.of(() -> getter.invoke(update)).getOrNull() != null)
                .forEach(getter -> {
                    String setMethod = getter.getName().replace("get", "set");
                    Try.run(() -> setters.get(setMethod).invoke(existing, getter.invoke(update)));
                });
    }

    private  static Map<String, Method> filterMethodsPartialName(String prefix, Method[] declaredMethods) {
        return Array.of(declaredMethods)
                .filter(e -> e.getName().contains(prefix))
                .map(e -> new Tuple2<>(e.getName(), e))
                .toJavaMap(LinkedHashMap::new, Tuple2::_1, Tuple2::_2);
    }
}
