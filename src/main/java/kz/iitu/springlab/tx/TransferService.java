package kz.iitu.springlab.tx;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository repository;

    public TransferService(AccountRepository repository) {
        this.repository = repository;
    }

    /** Task A: atomic transfer. */
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();
        from.debit(amount);
        to.credit(amount);
        repository.save(from);
        repository.save(to);
    }

    /** Task A: unchecked exception after debit, before credit -> rollback. */
    @Transactional
    public void transferFailAfterDebit(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new RuntimeException("Simulated failure after debit");
    }

    /** Task A: checked exception, default rules -> NO rollback (commit). */
    @Transactional
    public void transferCheckedNoRollback(Long fromId, Long toId, BigDecimal amount) throws Exception {
        Account from = repository.findById(fromId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new Exception("Simulated checked failure after debit");
    }

    /** Task A: checked exception with rollbackFor -> rollback. */
    @Transactional(rollbackFor = Exception.class)
    public void transferCheckedWithRollbackFor(Long fromId, Long toId, BigDecimal amount) throws Exception {
        Account from = repository.findById(fromId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new Exception("Simulated checked failure after debit");
    }
}
