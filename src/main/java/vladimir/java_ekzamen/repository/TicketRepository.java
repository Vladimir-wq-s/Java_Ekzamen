package vladimir.java_ekzamen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vladimir.java_ekzamen.entity.Ticket;
import vladimir.java_ekzamen.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByAuthor(User author, Pageable pageable);
    Page<Ticket> findByAssignee(User assignee, Pageable pageable);
    List<Ticket> findBySlaDeadlineBeforeAndEscalatedFalse(LocalDateTime now);
}