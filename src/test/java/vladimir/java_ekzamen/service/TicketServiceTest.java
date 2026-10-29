package vladimir.java_ekzamen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vladimir.java_ekzamen.entity.Ticket;
import vladimir.java_ekzamen.entity.TicketStatus;
import vladimir.java_ekzamen.entity.User;
import vladimir.java_ekzamen.repository.TicketRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private User testUser;
    private Ticket testTicket;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testTicket = new Ticket();
        testTicket.setId(1L);
        testTicket.setTitle("Test Ticket");
        testTicket.setDescription("Description");
        testTicket.setStatus(TicketStatus.NEW);
        testTicket.setAuthor(testUser);
    }

    @Test
    void testCreateTicket_Success() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        Ticket created = ticketService.createTicket("Test Ticket", "Description", testUser);

        assertNotNull(created);
        assertEquals("Test Ticket", created.getTitle());
        assertEquals(TicketStatus.NEW, created.getStatus());
        assertEquals(testUser, created.getAuthor());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testChangeStatus_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        Ticket updated = ticketService.changeStatus(1L, TicketStatus.CLOSED);

        assertEquals(TicketStatus.CLOSED, updated.getStatus());
        verify(ticketRepository, times(1)).save(testTicket);
    }

    @Test
    void testChangeStatus_TicketNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ticketService.changeStatus(99L, TicketStatus.CLOSED);
        });
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}