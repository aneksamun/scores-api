package co.uk.redpixel.scoring.core.config;

import co.uk.redpixel.scoring.core.HttpRequest;
import co.uk.redpixel.scoring.core.HttpResponse;
import co.uk.redpixel.scoring.core.http.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;

import static co.uk.redpixel.scoring.core.http.HttpMethod.GET;
import static co.uk.redpixel.scoring.core.http.HttpMethod.POST;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Endpoint<T> {

    @Getter
    private final String path;

    @Getter
    @EqualsAndHashCode.Exclude
    private final HttpMethod method;

    public final Function<HttpRequest, HttpResponse<T>> action;

    public static <T> Endpoint get(String path, Function<HttpRequest, HttpResponse<T>> action) {
        return new Endpoint<>(path, GET, action);
    }

    public static <T> Endpoint post(String path, Function<HttpRequest, HttpResponse<T>> action) {
        return new Endpoint<>(path, POST, action);
    }
}
