package kz.iitu.springlab.membership;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    // Variant 5: "today" is fixed, so the tests never depend on the real date
    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2026-11-10T09:00:00Z"), ZoneOffset.UTC);

    @Mock MemberRepository repository;

    MemberService service;

    @BeforeEach
    void setUp() {
        service = new MemberService(repository, FIXED_CLOCK);
    }

    @Test
    void aMembershipThatEndsInTheFutureIsActive() {
        given(repository.findById(1L))
                .willReturn(Optional.of(new Member(1L, "Aida", LocalDate.of(2027, 1, 1))));

        assertThat(service.statusOf(1L)).isEqualTo(MembershipStatus.ACTIVE);
    }

    @Test
    void anUnknownMemberIsReportedAsNotFound() {
        given(repository.findById(7L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.statusOf(7L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member 7 not found");
    }

    @Test
    void renewingAnActiveMembershipExtendsItFromItsExpiryDate() {
        given(repository.findById(1L))
                .willReturn(Optional.of(new Member(1L, "Aida", LocalDate.of(2027, 1, 1))));
        given(repository.save(org.mockito.ArgumentMatchers.any(Member.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        service.renew(1L, 3);

        var saved = ArgumentCaptor.forClass(Member.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().expiresOn()).isEqualTo(LocalDate.of(2027, 4, 1));
    }

    @Test
    void aMembershipThatExpiredYesterdayIsExpired() {
        given(repository.findById(2L))
                .willReturn(Optional.of(new Member(2L, "Dana", LocalDate.of(2026, 11, 9))));

        assertThat(service.statusOf(2L)).isEqualTo(MembershipStatus.EXPIRED);
    }@Test
    void renewingAnExpiredMembershipStartsFromToday() {
        given(repository.findById(2L))
                .willReturn(Optional.of(new Member(2L, "Dana", LocalDate.of(2026, 11, 9))));
        given(repository.save(org.mockito.ArgumentMatchers.any(Member.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        service.renew(2L, 1);

        var saved = ArgumentCaptor.forClass(Member.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().expiresOn()).isEqualTo(LocalDate.of(2026, 12, 10));
    }
}
