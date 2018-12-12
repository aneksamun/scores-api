package co.uk.redpixel.scoring.domain.model;

import co.uk.redpixel.scoring.core.HttpRequest;
import co.uk.redpixel.scoring.core.HttpResponse;
import lombok.val;

import java.util.function.Function;

import static co.uk.redpixel.scoring.core.HttpResponse.badRequest;
import static co.uk.redpixel.scoring.core.HttpResponse.ok;
import static co.uk.redpixel.scoring.core.HttpResponse.unauthorized;
import static co.uk.redpixel.scoring.core.http.Header.AUTHORIZATION;
import static java.lang.Integer.parseInt;

public interface UserAction<T> extends Function<HttpRequest, HttpResponse<T>> {

    static UserAction<String> welcome() {
        return request -> ok("Welcome to Scoring API!");
    }

    static UserAction<String> generateSessionKey() {
        return request -> ok(SessionKeys.getInstance().generate());
    }

    static UserAction<String> postScores(long tokenValidity) {
        return request -> request.getHeaderValue(AUTHORIZATION)
                .map(sessionKey -> {
                    try {
                        if (!SessionKeys.getInstance().contains(sessionKey))
                            return unauthorized("Session key is invalid");
                        else if (SessionKeys.getInstance().isExpired(sessionKey, tokenValidity)) {
                            return unauthorized("Session key is expired");
                        }

                        val parts = request.getUri().getPath().split("/");
                        val level = parseInt(parts[2]);
                        val user = parts[4];
                        val scores = parseInt(request.getBody());

                        Levels.getInstance().add(level, user, scores);

                        return ok("");

                    } catch (Exception e) {
                        return badRequest(e.getMessage());
                    }
                })
                .orElse(unauthorized("Session key is not present"));
    }

    static UserAction<String> getScoresPerLevel(int limit) {
        return request -> {
            val parts = request.getUri().getPath().split("/");
            val level = parseInt(parts[2]);

            return ok(Levels.getInstance().listHighScores(level, limit));
        };
    }
}
