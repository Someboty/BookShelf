package com.bookshop.repository.user;

import com.bookshop.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole getUserRoleByName(UserRole.RoleName roleName);
}
