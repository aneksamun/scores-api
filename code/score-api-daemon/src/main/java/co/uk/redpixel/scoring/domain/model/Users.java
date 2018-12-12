package co.uk.redpixel.scoring.domain.model;

import java.util.HashMap;
import java.util.stream.Collectors;

final class Users extends HashMap<String, Integer> {

    Users add(String user, int scores) {
        put(user, scores);
        return this;
    }

    String getTopScores(int limit) {
        return entrySet().stream()
                .sorted(Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
    }
}

