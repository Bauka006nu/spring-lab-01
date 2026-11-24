package kz.iitu.springlab.membership;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemberRepository {
    private final Map<Long, Member> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public MemberRepository() {
        save(new Member(null, "Aida", LocalDate.of(2027, 1, 1)));
        save(new Member(null, "Dana", LocalDate.of(2026, 11, 9)));
    }

    public Optional<Member> findById(long id) { return Optional.ofNullable(storage.get(id)); }

    public Member save(Member member) {
        long id = member.id() != null ? member.id() : sequence.incrementAndGet();
        Member stored = new Member(id, member.name(), member.expiresOn());
        storage.put(id, stored);
        return stored;
    }
}
