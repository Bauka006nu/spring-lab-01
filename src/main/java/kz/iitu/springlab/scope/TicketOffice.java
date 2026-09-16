package kz.iitu.springlab.scope;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class TicketOffice {

    private final Ticket directTicket;
    private final ObjectProvider<Ticket> ticketProvider;

    public TicketOffice(
            Ticket directTicket,
            ObjectProvider<Ticket> ticketProvider
    ) {
        this.directTicket = directTicket;
        this.ticketProvider = ticketProvider;
    }

    public String getTickets() {
        Ticket providerTicket1 = ticketProvider.getObject();
        Ticket providerTicket2 = ticketProvider.getObject();

        return "injectedDirectly: "
                + directTicket.getId()
                + " / "
                + directTicket.getId()
                + "\n"
                + "viaProvider: "
                + providerTicket1.getId()
                + " / "
                + providerTicket2.getId()
                + "\n"
                + "office: "
                + System.identityHashCode(this);
    }
}

