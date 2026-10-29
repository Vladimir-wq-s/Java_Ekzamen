package vladimir.java_ekzamen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vladimir.java_ekzamen.entity.Comment;
import vladimir.java_ekzamen.entity.Ticket;
import vladimir.java_ekzamen.entity.TicketCategory;
import vladimir.java_ekzamen.entity.TicketStatus;
import vladimir.java_ekzamen.entity.User;
import vladimir.java_ekzamen.repository.CommentRepository;
import vladimir.java_ekzamen.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;

    public TicketService(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
    }

    public Page<Ticket> findTicketsForUser(User user, Pageable pageable) {
        return ticketRepository.findByAuthor(user, pageable);
    }

    public Page<Ticket> findAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Ticket createTicket(String title, String description, TicketCategory category, User author) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);
        ticket.setStatus(TicketStatus.NEW);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSlaDeadline(LocalDateTime.now().plusHours(24));
        ticket.setEscalated(false);
        ticket.setAuthor(author);
        return ticketRepository.save(ticket);
    }

    public Ticket changeStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }

    public Ticket findTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Comment> getCommentsForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return commentRepository.findByTicketOrderByCreatedAtAsc(ticket);
    }

    public Comment addComment(Long ticketId, String text, User author) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTicket(ticket);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }
}