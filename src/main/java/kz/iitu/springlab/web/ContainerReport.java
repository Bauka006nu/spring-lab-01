package kz.iitu.springlab.web;

import kz.iitu.springlab.notify.Notifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ContainerReport {

    private final ApplicationContext context;

    public ContainerReport(ApplicationContext context) {
        this.context = context;
    }

    public String report() {
        String[] notifierBeans = context.getBeanNamesForType(Notifier.class);

        StringBuilder result = new StringBuilder();

        result.append("Bean definitions count: ")
                .append(context.getBeanDefinitionCount())
                .append("\n");

        result.append("Notifier beans: ")
                .append(Arrays.toString(notifierBeans))
                .append("\n");

        result.append("NotificationService bean: ")
                .append(context.getBean("notificationService").getClass().getSimpleName())
                .append("\n");

        result.append("All beans count: ")
                .append(context.getBeanDefinitionCount());

        return result.toString();
    }
}
