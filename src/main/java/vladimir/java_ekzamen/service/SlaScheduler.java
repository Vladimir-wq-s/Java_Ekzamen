package vladimir.java_ekzamen.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vladimir.java_ekzamen.entity.Ticket;
import vladimir.java_ekzamen.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SlaScheduler {

    private final TicketRepository ticketRepository;

    public SlaScheduler(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void checkSla() {
        List<Ticket> overdue = ticketRepository.findBySlaDeadlineBeforeAndEscalatedFalse(LocalDateTime.now());
        for (Ticket ticket : overdue) {
            ticket.setEscalated(true);
            ticketRepository.save(ticket);
            System.out.println("SLA violated for ticket ID: " + ticket.getId());
        }
    }
}