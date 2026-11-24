package kz.iitu.springlab.membership;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/** The application reads "now" from this bean, so tests can replace it with a fixed clock. */
@Configuration(proxyBeanMethods = false)
public class ClockConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
