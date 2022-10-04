package crdb.jmsapp.service;
import crdb.jmsapp.domain.Customer;
import crdb.jmsapp.domain.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class MyRunner implements CommandLineRunner {
private final CustomerService customerService;
public  MyRunner(CustomerService customerService){
    this.customerService = customerService;
}
    @Override
    public void run(String... args) throws Exception {
//        customerService.saveRoles(new Roles(null, "ROLE_USER"));
//        customerService.saveRoles(new Roles(null, "ROLE_MANAGER"));
//        customerService.saveRoles(new Roles(null, "ROLE_ADMIN"));
//        customerService.saveRoles(new Roles(null, "ROLE_SUPER_ADMIN"));
//
////            Add customers
//        customerService.saveCustomer(new Customer(null, "Kaizen Ella", "el@gmail.om", "ella", "1234", new ArrayList<>()));
//        customerService.saveCustomer(new Customer(null, "Otile Brown", "el@gmail.om", "brown", "qwerty", new ArrayList<>()));
//        customerService.saveCustomer(new Customer(null, "Innocent Lac", "el@gmail.om", "lac", "1111", new ArrayList<>()));
//        customerService.saveCustomer(new Customer(null, "Jax Larson", "el@gmail.om", "jax", "12374", new ArrayList<>()));
//        customerService.saveCustomer(new Customer(null, "Te Ella", "el@gmail.om", "tella", "1234", new ArrayList<>()));
//
////            Add roles to users
//        customerService.addRoleToCustomer("tella", "ROLE_ADMIN");
//        customerService.addRoleToCustomer("tella", "ROLE_USER");
//        customerService.addRoleToCustomer("tella", "ROLE_MANAGER");
//        customerService.addRoleToCustomer("jax", "ROLE_USER");
//        customerService.addRoleToCustomer("brown", "ROLE_MANAGER");
//        customerService.addRoleToCustomer("lac", "ROLE_ADMIN");
//
//        customerService.addRoleToCustomer("ella", "ROLE_ADMIN");
//
//        customerService.addRoleToCustomer("ella", "ROLE_ADMIN");



    }
}
