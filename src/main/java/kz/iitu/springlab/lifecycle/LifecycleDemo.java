package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import kz.iitu.springlab.config.FormatConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LifecycleDemo {

    private final DateTimeFormatter formatter;

    public LifecycleDemo(DateTimeFormatter formatter) {
        this.formatter = formatter;
        System.out.println(
                "LifecycleDemo: constructor "
                        + LocalDateTime.now().format(formatter)
        );
    }

    @PostConstruct
    public void init() {
        System.out.println(
                "LifecycleDemo: @PostConstruct "
                        + LocalDateTime.now().format(formatter)
        );
    }

    @PreDestroy
    public void destroy() {
        System.out.println(
                "LifecycleDemo: @PreDestroy "
                        + LocalDateTime.now().format(formatter)
        );
    }

    public String status() {
        return "LifecycleDemo is alive: "
                + LocalDateTime.now().format(formatter);
    }
}


