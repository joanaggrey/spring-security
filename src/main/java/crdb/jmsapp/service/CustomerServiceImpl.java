package crdb.jmsapp.service;
import crdb.jmsapp.domain.Customer;
import crdb.jmsapp.domain.Roles;
import crdb.jmsapp.repo.CustomerRepository;
import crdb.jmsapp.repo.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private  final CustomerRepository customerRepository;
    private  final RolesRepository rolesRepository;
@Lazy
@Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if(customer == null){
            log.error("Customer not found in the database");
            throw  new UsernameNotFoundException("Customer not found in the database");
        }
        else{
            log.info("Customer found in the database {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        customer.getRoles().forEach((roles -> {
            authorities.add(new SimpleGrantedAuthority(roles.getName()));
        }));
        return new User(customer.getUsername(), customer.getPassword(), authorities);
    }

    @Override
    public Object saveCustomer(Customer customer) {
        log.info("Saving new user {} to the database",customer.getName());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
//        check if the user exists in the db
        Customer existingCustomer = customerRepository.findByUsername(customer.getUsername());
        if (existingCustomer == null){
            return customerRepository.save(customer);
        }else{
            return "Customer already exists";
        }
    }

    @Override
    public Roles saveRoles(Roles roles) {
        log.info("Saving a new role {} to the database",roles.getName());
        return rolesRepository.save(roles);
    }

    @Override
    public Object addRoleToCustomer(String username, String roleName) {
        log.info("Adding a new role {} to customer {}", roleName, username);
        Customer customer = customerRepository.findByUsername(username);
        Roles role = rolesRepository.findByName(roleName);
        System.out.println("role");
        System.out.print(role);
        Collection<Roles> roles = customer.getRoles();
        System.out.print("roles");
        System.out.print(roles);
        if(!(roles.contains(role))){
            customer.getRoles().add(role);
            return "Role added to customers";
        }else{
            return "Role exists";
        }

    }

    @Override
    public Customer getCustomer(String username) {
        log.info("Fetching customer {}",username);
        return customerRepository.findByUsername(username);
    }

    //    Return customers in pages
    @Override
    public List<Customer> getCustomers() {
        log.info("Fetching all users");
        return customerRepository.findAll();
    }


}
