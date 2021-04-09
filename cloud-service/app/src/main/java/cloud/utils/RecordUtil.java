package cloud.utils;

import com.aerospike.client.Bin;
import com.aerospike.client.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Util that takes care the conversion between Aerospike DB formats to the custom class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts record {@link Record} to the given class. <br/>
     * The conversion type and the return type should be the same.
     *
     * @param record to be converted
     * @param type   desired class
     * @param <T>    the desired class and return type
     * @return converted value wrapped into {@link Optional}
     */
    public static <T> T recordTo(Record record, Class<T> type) {
        return Optional.ofNullable(record)
                .map(rec -> rec.bins)
                .map(bins -> mapper.convertValue(bins, type))
                .orElse(null);
    }

    /**
     * The method converts the given class into the Bin {@link Bin}.  <br/>
     * It uses the reflection to access the given class's getter and setter methods not to copy each field individually.
     * Then with the help of {@link #filterMethodsPartialName} method,<br/>
     * filters the getter and setter methods and performs the conversion.
     *
     * @param clazz to be converted into Bin {@link Bin}
     * @param <T>   type of the class
     * @return arrays of bin where each bin corresponds to the single field of the clazz
     */
    public static <T> Bin[] classToBin(T clazz) {
        Method[] declaredMethods = clazz.getClass().getDeclaredMethods();
        Map<String, Method> methods = filterMethodsPartialName("get", declaredMethods);
        return methods.values()
                .stream()
                .map(e -> new Tuple2<>(replaceName("get", e.getName()),
                        Try.of(() -> e.invoke(clazz)).getOrNull()))
                .filter(e -> e._2 != null)
                .map(pair -> new Bin(pair._1, pair._2))
                .toArray(Bin[]::new);
    }

    /**
     * Remove prefix from the string.
     *
     * @param prefix to be removed
     * @param name   from which the prefix should be removed
     * @return processed string
     */
    private static String replaceName(String prefix, String name) {
        String processed = name.replace(prefix, "");
        return Character.toLowerCase(processed.charAt(0)) + processed.substring(1);
    }

    /**
     * Updates the object using reflection, the both instances should have the same type.<br/>
     * It uses the reflection to access the given class's getter and setter methods not to copy each field individually.
     *
     * @param existing instance to be updated
     * @param update   method copies non null fields from update into existing
     * @param <T>      of instances
     */
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

    /**
     * Filters the methods that contain the given prefix.
     *
     * @param prefix          that should be present in the method name
     * @param declaredMethods methods extracted by reflection or any other similar tool
     * @return the map of methods where the key corresponds to the method name and the value corresponds to the method.
     */
    private static Map<String, Method> filterMethodsPartialName(String prefix, Method[] declaredMethods) {
        return Array.of(declaredMethods)
                .filter(e -> e.getName().contains(prefix))
                .map(e -> new Tuple2<>(e.getName(), e))
                .toJavaMap(LinkedHashMap::new, Tuple2::_1, Tuple2::_2);
    }
}
