package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class IsolationTest extends AbstractTxTest {

    @Autowired PlatformTransactionManager txManager;

    /** Returns {firstRead, secondRead} of Alice's balance seen by transaction 1. */
    private BigDecimal[] run(Isolation isolation) throws Exception {
        TransactionTemplate t1 = new TransactionTemplate(txManager);
        t1.setIsolationLevel(isolation.value());

        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch otherCommitted = new CountDownLatch(1);
        BigDecimal[] reads = new BigDecimal[2];

        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<?> f = ex.submit(() -> t1.executeWithoutResult(s -> {
            reads[0] = balance("Alice");               // JDBC read: bypasses JPA 1st-level cache
            firstReadDone.countDown();
            try { otherCommitted.await(5, TimeUnit.SECONDS); }
            catch (InterruptedException e) { throw new RuntimeException(e); }
            reads[1] = balance("Alice");
        }));

        firstReadDone.await(5, TimeUnit.SECONDS);
        // Transaction 2: update and commit
        new TransactionTemplate(txManager).executeWithoutResult(s ->
                jdbc.update("UPDATE accounts SET balance = balance + 50 WHERE owner_name = 'Alice'"));
        otherCommitted.countDown();

        f.get(10, TimeUnit.SECONDS);
        ex.shutdown();
        return reads;
    }

    @Test
    void readCommittedSeesChangedValue() throws Exception {
        BigDecimal[] r = run(Isolation.READ_COMMITTED);
        System.out.println("READ_COMMITTED   1st=" + r[0] + " 2nd=" + r[1]);
        assertNotEquals(0, r[0].compareTo(r[1]));   // non-repeatable read happens
    }

    @Test
    void repeatableReadObservation() throws Exception {
        BigDecimal[] r = run(Isolation.REPEATABLE_READ);
        // Do not assume: record what YOUR database/version actually does.
        System.out.println("REPEATABLE_READ  1st=" + r[0] + " 2nd=" + r[1]);
    }
}
