package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.UserRole;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserRoleService {
    private final IUserRoleRepository roleRepository;

    public final void save(final UserRole role) {
        log.info("Saving new Role");
        roleRepository.save(role);
    }

    public final UserRole getById(final Long id) {
        log.info("Fetching role with id: {}", id);
        return roleRepository.findById(id).orElse(new UserRole());
    }

    public final UserRole getByName(final String name) {
        log.info("Fetching role with name: {}", name);
        return roleRepository.findByName(name).orElse(new UserRole());
    }

    public final void update(final UserRole role) {
        log.info("Updating role with id: {}", role.getId());
        roleRepository.save(role);
    }

    public final void delete(final Long id) {
        log.info("Deleting role with id: {}", id);
        roleRepository.deleteById(id);
    }

    public final List<UserRole> getAll() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }
}
