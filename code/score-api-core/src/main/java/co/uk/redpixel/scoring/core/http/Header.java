package co.uk.redpixel.scoring.core.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Header {
    AUTHORIZATION("Authorization"),
    CONTENT_LENGTH("content-length");

    @Getter
    private final String key;
}
