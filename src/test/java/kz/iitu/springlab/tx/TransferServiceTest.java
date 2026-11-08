package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest extends AbstractTxTest {

    @Autowired TransferService service;

    private static BigDecimal bd(String v) { return new BigDecimal(v); }

    @Test
    void successfulTransfer() {
        service.transfer(id("Alice"), id("Bob"), bd("100.00"));
        assertEquals(0, bd("900.00").compareTo(balance("Alice")));
        assertEquals(0, bd("600.00").compareTo(balance("Bob")));
    }

    @Test
    void insufficientFundsRollsBack() {
        assertThrows(IllegalStateException.class,
                () -> service.transfer(id("Alice"), id("Bob"), bd("5000.00")));
        assertEquals(0, bd("1000.00").compareTo(balance("Alice")));
        assertEquals(0, bd("500.00").compareTo(balance("Bob")));
    }

    @Test
    void uncheckedExceptionAfterDebitRollsBack() {
        assertThrows(RuntimeException.class,
                () -> service.transferFailAfterDebit(id("Alice"), id("Bob"), bd("100.00")));
        assertEquals(0, bd("1000.00").compareTo(balance("Alice")));
        assertEquals(0, bd("500.00").compareTo(balance("Bob")));
    }

    @Test
    void checkedExceptionCommitsByDefault() {
        assertThrows(Exception.class,
                () -> service.transferCheckedNoRollback(id("Alice"), id("Bob"), bd("100.00")));
        // debit was committed, credit never happened: money lost!
        assertEquals(0, bd("900.00").compareTo(balance("Alice")));
        assertEquals(0, bd("500.00").compareTo(balance("Bob")));
    }

    @Test
    void checkedExceptionWithRollbackForRollsBack() {
        assertThrows(Exception.class,
                () -> service.transferCheckedWithRollbackFor(id("Alice"), id("Bob"), bd("100.00")));
        assertEquals(0, bd("1000.00").compareTo(balance("Alice")));
    }
}
