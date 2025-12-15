package fitness.club.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private JsonUtil() {}

    public static <T> T read(HttpServletRequest request, Class<T> clazz) throws IOException {
        try (InputStream is = request.getInputStream()) {
            return MAPPER.readValue(is, clazz);
        }
    }

    public static void write(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        if (body != null) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(MAPPER.writeValueAsString(body));
        }
    }
}
