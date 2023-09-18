package com.bookshop.repository.role;

import com.bookshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getUserRoleByName(Role.RoleName roleName);
}
