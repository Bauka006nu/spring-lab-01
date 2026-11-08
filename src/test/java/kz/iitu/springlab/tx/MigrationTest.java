package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MigrationTest extends AbstractTxTest {

    @Test
    void migrationsApplied() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT \"version\", \"description\", \"success\" FROM \"flyway_schema_history\" ORDER BY \"installed_rank\"");
        rows.forEach(System.out::println);
        assertTrue(rows.size() >= 3);
        assertTrue(rows.stream().allMatch(r -> Boolean.TRUE.equals(r.get("success"))));
    }
}
