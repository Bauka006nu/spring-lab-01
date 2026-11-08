package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OptimisticLockingTest extends AbstractTxTest {

    @Test
    void staleUpdateIsRejected() {
        Long aliceId = id("Alice");

        // Two independent copies (each findById runs in its own tx/persistence context -> detached)
        Account copy1 = accounts.findById(aliceId).orElseThrow();
        Account copy2 = accounts.findById(aliceId).orElseThrow();
        assertEquals(copy1.getVersion(), copy2.getVersion());

        copy1.credit(new BigDecimal("10.00"));
        accounts.save(copy1);                                   // commits, version 0 -> 1

        copy2.credit(new BigDecimal("20.00"));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> accounts.save(copy2));

        // First update survived; second did not overwrite it
        assertEquals(0, new BigDecimal("1010.00").compareTo(balance("Alice")));
        System.out.println("Optimistic lock conflict detected, Alice balance = " + balance("Alice"));
    }
}
