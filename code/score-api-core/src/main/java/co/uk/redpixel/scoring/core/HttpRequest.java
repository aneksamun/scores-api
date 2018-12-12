package co.uk.redpixel.scoring.core;

import co.uk.redpixel.scoring.core.http.Header;
import co.uk.redpixel.scoring.core.http.HttpMethod;
import lombok.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static co.uk.redpixel.scoring.core.http.Header.CONTENT_LENGTH;
import static java.util.Collections.unmodifiableMap;
import static lombok.AccessLevel.PRIVATE;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
public final class HttpRequest {

    @Getter
    private final URI uri;

    @Getter
    private final String version;

    @Getter
    private final HttpMethod method;

    @Getter
    private final Map<String, String> headers;

    @Getter
    private final String body;

    public Optional<String> getHeaderValue(Header header) {
        return Optional.ofNullable(headers.get(header.getKey()));
    }

    static HttpRequest of(String content) {
        val lines = content.split("\r\n");
        val firstLine = lines[0].split("[ ]");
        val method = HttpMethod.valueOf(firstLine[0]);
        val uri = URI.create(firstLine[1]);
        val version = firstLine[2];

        int index = 1;
        val headers = new HashMap<String, String>();

        for (; index < lines.length; index++) {
            if (lines[index].isEmpty()) {
                break;
            } else if (lines[index].contains(":")) {
                val header = lines[index];
                val key = header.substring(0, header.indexOf(":"));
                val value = header.substring(header.indexOf(": ") + 2);
                headers.put(key, value);
            }
        }

        StringBuilder bodyBuilder = new StringBuilder();

        if (headers.containsKey(CONTENT_LENGTH.getKey())) {
            for (; index < lines.length; index++) {
                if (!lines[index].isEmpty()) {
                    bodyBuilder.append(lines[index]);
                }
            }
        }

        return new HttpRequest(uri, version, method, unmodifiableMap(headers), bodyBuilder.toString());
    }
}
