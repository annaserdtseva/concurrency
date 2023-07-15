package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class RestaurantService {

    static final String STAT_FORMAT = "%s - %d";
    private final Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private final ConcurrentHashMap<String, LongAdder> stat;

    public RestaurantService() {
        this.stat = new ConcurrentHashMap<>(
            restaurantMap.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, v -> new LongAdder())));
    }

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        stat.get(restaurantName).increment();
    }

    public Set<String> printStat() {
        return stat.entrySet().stream()
            .map(v -> String.format(STAT_FORMAT, v.getKey(), v.getValue().longValue()))
            .collect(Collectors.toSet());
    }
}
