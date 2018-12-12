package co.uk.redpixel.scoring.common;

import lombok.AllArgsConstructor;

import static java.lang.String.format;

@AllArgsConstructor
public enum Error {
    CONFIG_LOAD_ERROR("Cannot load configuration: %s");

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorMessage(Object... args) {
        return format(errorMessage, args);
    }
}
