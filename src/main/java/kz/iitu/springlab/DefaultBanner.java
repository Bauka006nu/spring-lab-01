package kz.iitu.springlab;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev & !prod")
public class DefaultBanner implements EnvironmentBanner {

    @Override
    public String text() {
        return "NO PROFILE";
    }
}

