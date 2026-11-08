package kz.iitu.springlab.tx;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropagationService {

    private final AuditService auditService;

    public PropagationService(AuditService auditService) {
        this.auditService = auditService;
    }

    @Transactional
    public void outerWithRequired(boolean fail) {
        auditService.logRequired("audit via REQUIRED");
        if (fail) throw new IllegalStateException("Outer failure");
    }

    @Transactional
    public void outerWithRequiresNew(boolean fail) {
        auditService.logRequiresNew("audit via REQUIRES_NEW");
        if (fail) throw new IllegalStateException("Outer failure");
    }
}
