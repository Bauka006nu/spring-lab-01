package kz.iitu.springlab.tx;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;

/** Tests are intentionally NOT @Transactional: we want real commits and rollbacks. */
@SpringBootTest
abstract class AbstractTxTest {

    @Autowired protected JdbcTemplate jdbc;
    @Autowired protected AccountRepository accounts;

    @BeforeEach
    void resetData() {
        jdbc.update("DELETE FROM audit_log");
        jdbc.update("UPDATE accounts SET balance = 1000.00, version = 0 WHERE owner_name = 'Alice'");
        jdbc.update("UPDATE accounts SET balance = 500.00, version = 0 WHERE owner_name = 'Bob'");
    }

    protected Long id(String owner) { return accounts.findByOwnerName(owner).orElseThrow().getId(); }

    protected BigDecimal balance(String owner) {
        return jdbc.queryForObject("SELECT balance FROM accounts WHERE owner_name = ?", BigDecimal.class, owner);
    }
}
