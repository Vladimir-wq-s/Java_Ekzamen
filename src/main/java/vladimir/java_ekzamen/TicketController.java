package vladimir.java_ekzamen.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vladimir.java_ekzamen.entity.Ticket;
import vladimir.java_ekzamen.entity.TicketCategory;
import vladimir.java_ekzamen.entity.User;
import vladimir.java_ekzamen.repository.UserRepository;
import vladimir.java_ekzamen.security.UserDetailsImpl;
import vladimir.java_ekzamen.service.TicketService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listTickets(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "createdAt") String sortBy,
                              Model model) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Page<Ticket> ticketPage;

        if (currentUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN") || r.getName().equals("ROLE_MANAGER"))) {
            ticketPage = ticketService.findAllTickets(PageRequest.of(page - 1, size, Sort.by(sortBy).descending()));
        } else {
            ticketPage = ticketService.findTicketsForUser(currentUser, PageRequest.of(page - 1, size, Sort.by(sortBy).descending()));
        }

        model.addAttribute("ticketPage", ticketPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);

        int totalPages = ticketPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "tickets";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticket-form";
    }

    @PostMapping("/new")
    public String createTicket(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam TicketCategory category) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        ticketService.createTicket(title, description, category, currentUser);
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String status) {
        ticketService.changeStatus(id, vladimir.java_ekzamen.entity.TicketStatus.valueOf(status));
        return "redirect:/tickets";
    }

    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findTicketById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", ticketService.getCommentsForTicket(id));
        return "ticket-details";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam String text,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        ticketService.addComment(id, text, currentUser);
        return "redirect:/tickets/" + id;
    }
}