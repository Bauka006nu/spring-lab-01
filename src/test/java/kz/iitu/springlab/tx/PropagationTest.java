package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PropagationTest extends AbstractTxTest {

    @Autowired PropagationService service;

    private int auditCount() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM audit_log", Integer.class);
    }

    @Test
    void requiredAuditIsRolledBackWithOuter() {
        assertThrows(IllegalStateException.class, () -> service.outerWithRequired(true));
        assertEquals(0, auditCount());
    }

    @Test
    void requiresNewAuditSurvivesOuterRollback() {
        assertThrows(IllegalStateException.class, () -> service.outerWithRequiresNew(true));
        assertEquals(1, auditCount());
    }

    @Test
    void bothCommitWhenNoFailure() {
        service.outerWithRequired(false);
        service.outerWithRequiresNew(false);
        assertEquals(2, auditCount());
    }
}
