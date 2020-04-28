package player;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class StreamUtils {
    public static <V> Stream<V> iterateByNext(
        List<V> list,
        BiConsumer<V, V> selector
    ) {
        if (list.size() > 1) for (int i = 0; i < list.size() - 1; i++) {
            selector.accept(list.get(i), list.get(i + 1));
        }
        return list.stream();
    }

    public static <T, R> Collector<T, ?, Stream<T>> distinctByKey(Function<T, R> keyExtractor) {
        return Collectors.collectingAndThen(
            toMap(
                keyExtractor,
                t -> t,
                (t1, t2) -> t1
            ),
            (Map<R, T> map) -> map.values().stream()
        );
    }
}