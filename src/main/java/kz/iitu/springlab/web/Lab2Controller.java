package kz.iitu.springlab.web;

import kz.iitu.springlab.lifecycle.LifecycleDemo;
import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    private final NotificationService notificationService;
    private final TicketOffice ticketOffice;
    private final ContainerReport containerReport;
    private final LifecycleDemo lifecycleDemo;

    public Lab2Controller(
            NotificationService notificationService,
            TicketOffice ticketOffice,
            ContainerReport containerReport,
            LifecycleDemo lifecycleDemo
    ) {
        this.notificationService = notificationService;
        this.ticketOffice = ticketOffice;
        this.containerReport = containerReport;
        this.lifecycleDemo = lifecycleDemo;
    }

    @GetMapping("/notify")
    public Map<String, Object> notify(@RequestParam String text) {
        return Map.of(
                "primary", notificationService.viaPrimary(text),
                "console", notificationService.viaConsole(text),
                "all", notificationService.viaAll(text),
                "names", notificationService.names()
        );
    }

    @GetMapping("/scopes")
    public String scopes() {
        return ticketOffice.getTickets();
    }

    @GetMapping("/container")
    public String container() {
        return containerReport.report();
    }

    @GetMapping("/custom")
    public String custom(@RequestParam String text) {
        return notificationService.viaTruncating(text);
    }

    @GetMapping("/lifecycle")
    public String lifecycle() {
        return lifecycleDemo.status();
    }
}


