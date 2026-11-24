package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired AccountRepository repository;
    @Autowired TestEntityManager em;

    @Test
    void findsAnAccountByItsOwnerName() {
        em.persist(new Account("Carol", new BigDecimal("250.00")));
        em.flush();
        em.clear();          // force the query to hit the database

        var found = repository.findByOwnerName("Carol");

        assertThat(found).isPresent();
        assertThat(found.get().getBalance()).isEqualByComparingTo("250.00");
    }

    @Test
    void doesNotFindAnOwnerWhoWasNeverSaved() {
        em.persist(new Account("Carol", new BigDecimal("250.00")));
        em.flush();
        em.clear();

        assertThat(repository.findByOwnerName("Nobody")).isEmpty();
    }
}
