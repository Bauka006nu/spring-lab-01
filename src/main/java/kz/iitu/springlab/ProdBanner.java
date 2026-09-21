package kz.iitu.springlab;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdBanner implements EnvironmentBanner {

    @Override
    public String text() {
        return "PROD environment";
    }
}

