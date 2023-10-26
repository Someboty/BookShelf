package com.bookshelf.repository.role;

import com.bookshelf.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getUserRoleByName(Role.RoleName roleName);
}
