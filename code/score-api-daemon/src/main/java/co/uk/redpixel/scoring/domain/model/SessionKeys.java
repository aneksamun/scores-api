package co.uk.redpixel.scoring.domain.model;

import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class SessionKeys {

    private static final ConcurrentHashMap<String, Long> tokens =
            new ConcurrentHashMap<>();

    private static class SessionKeysConstructor {
        private static final SessionKeys INSTANCE = new SessionKeys();
    }

    static SessionKeys getInstance() {
        return SessionKeysConstructor.INSTANCE;
    }

    String generate() {
        String token = randomUUID().toString().replace("-", "");
        long timestamp = System.currentTimeMillis();
        tokens.compute(token, (key, old) -> timestamp);
        return token;
    }

    boolean contains(String token) {
        return tokens.containsKey(token);
    }

    boolean isExpired(String token, long expiresAfter) {
        val expiresAt = tokens.computeIfPresent(token, (key, timestamp) -> timestamp + expiresAfter);
        return Optional.ofNullable(expiresAt)
                .map(at -> at <= System.currentTimeMillis())
                .orElse(false);
    }
}
