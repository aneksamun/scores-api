package co.uk.redpixel.scoring.core.config;

import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Arrays;
import java.util.HashSet;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Routes extends HashSet<Endpoint> {

    public static Routes of(Endpoint... endpoints) {
        val routes = new Routes();
        routes.addAll(Arrays.asList(endpoints));
        return routes;
    }

    public static Routes empty() {
        return new Routes();
    }
}
