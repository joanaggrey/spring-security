package crdb.jmsapp.repo;

import crdb.jmsapp.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String username);

}
