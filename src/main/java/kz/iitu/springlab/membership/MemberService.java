package kz.iitu.springlab.membership;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class MemberService {

    private final MemberRepository repository;
    private final Clock clock;

    public MemberService(MemberRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Member get(long id) {
        return repository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    public MembershipStatus statusOf(long id) {
        LocalDate expiresOn = get(id).expiresOn();
        LocalDate today = LocalDate.now(clock);
        if (expiresOn.isBefore(today)) {
            return MembershipStatus.EXPIRED;
        }
        if (expiresOn.isEqual(today)) {
            return MembershipStatus.EXPIRES_TODAY;
        }
        return MembershipStatus.ACTIVE;
    }

    /** An active membership is extended from its expiry date; an expired one starts again from today. */
    public Member renew(long id, int months) {
        Member member = get(id);
        LocalDate today = LocalDate.now(clock);
        LocalDate base = member.expiresOn().isBefore(today) ? today : member.expiresOn();
        return repository.save(new Member(member.id(), member.name(), base.plusMonths(months)));
    }
}
