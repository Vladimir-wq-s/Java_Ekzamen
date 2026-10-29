package vladimir.java_ekzamen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vladimir.java_ekzamen.entity.Comment;
import vladimir.java_ekzamen.entity.Ticket;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicketOrderByCreatedAtAsc(Ticket ticket);
}