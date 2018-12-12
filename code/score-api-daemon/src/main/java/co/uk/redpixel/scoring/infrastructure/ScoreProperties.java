package co.uk.redpixel.scoring.infrastructure;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static co.uk.redpixel.scoring.common.Error.CONFIG_LOAD_ERROR;

public final class ScoreProperties extends Properties {

    private static final int DEFAULT_MAX_SCORES = 15;
    private static final long DEFAULT_TOKEN_VALIDITY = 600000;

    private static final String PROPERTIES_FILE = "application.properties";
    private static final String SCORES_PER_PAGE = "scores.per.page";
    private static final String TOKEN_VALIDITY = "token.validity";

    private ScoreProperties() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            try (InputStream stream = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
                this.load(stream);
            }
        } catch (Exception e) {
            throw new ConfigurationException(CONFIG_LOAD_ERROR, e.getMessage());
        }
    }

    public int getScoresLimit() {
        return Optional.ofNullable(getProperty(SCORES_PER_PAGE))
                .map(Integer::parseInt)
                .orElse(DEFAULT_MAX_SCORES);
    }

    public long getTokenValidity() {
        return Optional.ofNullable(getProperty(TOKEN_VALIDITY))
                .map(Long::parseLong)
                .orElse(DEFAULT_TOKEN_VALIDITY);
    }

    public static ScoreProperties load() {
        return new ScoreProperties();
    }
}
