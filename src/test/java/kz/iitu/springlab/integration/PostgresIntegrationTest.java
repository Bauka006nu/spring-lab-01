package kz.iitu.springlab.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs the whole application against a real PostgreSQL started in a container.
 * Skipped (not failed) on a machine without Docker - see docs/lab12-evidence.md.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Value("${local.server.port}") int port;
    @Autowired JdbcTemplate jdbc;

    @Test
    void flywayMigrationsRunOnARealPostgres() {
        Integer applied = jdbc.queryForObject(
                "select count(*) from flyway_schema_history where success = true and version is not null",
                Integer.class);
        Integer users = jdbc.queryForObject("select count(*) from app_user", Integer.class);

        assertThat(applied).isEqualTo(5);
        assertThat(users).isEqualTo(2);
    }

    @Test
    void anAnonymousRequestForAProtectedPageIsRedirectedToTheLoginForm() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/books")).build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        assertThat(response.statusCode()).isEqualTo(302);
        assertThat(response.headers().firstValue("Location")).hasValueSatisfying(l -> assertThat(l).endsWith("/login"));
    }
}
