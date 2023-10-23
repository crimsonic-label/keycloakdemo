package atd.demo.keycloakdemo;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

  private final CustomerRepository customerRepository;

  @GetMapping(path = "/")
  public String index() {
    return "external";
  }

  @GetMapping(path = "/customers")
  public String customers(Principal principal, Model model) {
    addCustomers();
    model.addAttribute("customers", customerRepository.findAll());
    model.addAttribute("username", principal.getName());
    return "customers";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

  private void addCustomers() {
    Customer customer = new Customer();
    customer.setAddress("1111 foo blvd");
    customer.setName("Foo Industries");
    customer.setServiceRendered("Important services");
    customerRepository.save(customer);
  }
}
