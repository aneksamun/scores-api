package co.uk.redpixel.scoring;

import co.uk.redpixel.scoring.core.HttpServer;
import co.uk.redpixel.scoring.core.config.Routes;
import co.uk.redpixel.scoring.infrastructure.ScoreProperties;
import lombok.val;

import static co.uk.redpixel.scoring.core.config.Endpoint.get;
import static co.uk.redpixel.scoring.core.config.Endpoint.post;
import static co.uk.redpixel.scoring.domain.model.UserAction.*;

public class Application {

    public static void main(String[] args) {
        try {
            val properties = ScoreProperties.load();

            val routes = Routes.of(
                get("/$", welcome()),
                post("/login$", generateSessionKey()),
                post("/levels/[0-9]*/users/[A-Za-z0-9]+$", postScores(properties.getTokenValidity())),
                get("/levels/[0-9]*/scores$", getScoresPerLevel(properties.getScoresLimit()))
            );

            try (val server = HttpServer.on(8090).with(routes).create()) {
                System.out.printf("Starting server on port %d...\n", server.getPort());
                System.out.println("Press Ctrl+C to exit");
                server.start();
            }

        } catch (Exception e) {
            System.out.printf("An error occurred: %s\n", e.getMessage());
        }
    }
}
