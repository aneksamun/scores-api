package co.uk.redpixel.scoring.domain.model;

import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class Levels {

    private static final ConcurrentHashMap<Integer, Users> records =
            new ConcurrentHashMap<>();

    private static class LevelsConstructor {
        private static final Levels INSTANCE = new Levels();
    }

    static Levels getInstance() {
        return LevelsConstructor.INSTANCE;
    }

    String listHighScores(int level, int limit) {
        return Optional.ofNullable(records.get(level))
                .map(record -> record.getTopScores(limit))
                .orElse("");
    }

    void add(int level, String user, int scores) {
        records.compute(level, (currentLevel, users) -> Optional.ofNullable(users)
                .map(calculateScores(user, scores))
                .orElse(new Users().add(user, scores)));
    }

    private static Function<Users, Users> calculateScores(String user, int scores) {
        return users -> {
            users.compute(user, (currentUser, currentScores) -> (isNull(currentScores) ? 0 : currentScores) + scores);
            return users;
        };
    }
}
