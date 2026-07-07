package es.cic.curso25.back.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for the REST API.
 *
 * <p>Allowed origins:
 * <ul>
 *   <li><code>http://localhost:4200</code> — Angular CLI default dev server</li>
 *   <li><code>http://localhost:5173</code> — Vite dev server (this portfolio)</li>
 *   <li>Any origin listed in the <code>app.cors.origins</code> property
 *       (overridable via the <code>APP_CORS_ORIGINS</code> env var, comma-separated)</li>
 * </ul>
 */
@Configuration
public class CorsConfig {

    private static final List<String> DEFAULT_ORIGINS = List.of(
            "http://localhost:4200",
            "http://localhost:5173"
    );

    @Value("${app.cors.origins:}")
    private String extraOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        List<String> allowed = DEFAULT_ORIGINS;
        if (extraOrigins != null && !extraOrigins.isBlank()) {
            List<String> parsed = Arrays.stream(extraOrigins.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            allowed = Stream
                    .concat(DEFAULT_ORIGINS.stream(), parsed.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

        final List<String> origins = allowed;
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(origins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
