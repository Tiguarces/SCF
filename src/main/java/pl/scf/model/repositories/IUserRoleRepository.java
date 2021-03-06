package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.UserRole;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {
     UserRole findByName(String name);
}
