package crdb.jmsapp.service;

import crdb.jmsapp.domain.Customer;
import crdb.jmsapp.domain.Roles;

import java.util.List;

public interface CustomerService {
    Object saveCustomer(Customer customer);
    Roles saveRoles(Roles roles);
    Object addRoleToCustomer(String username, String roleName);
    Customer getCustomer(String username);
    List<Customer> getCustomers();

}
