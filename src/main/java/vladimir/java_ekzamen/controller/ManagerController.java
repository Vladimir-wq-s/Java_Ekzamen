package vladimir.java_ekzamen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/tickets")
    public String allTickets() {
        return "redirect:/tickets";
    }
}