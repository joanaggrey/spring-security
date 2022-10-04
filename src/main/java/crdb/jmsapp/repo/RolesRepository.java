package crdb.jmsapp.repo;

import crdb.jmsapp.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Roles findByName(String name);
}
