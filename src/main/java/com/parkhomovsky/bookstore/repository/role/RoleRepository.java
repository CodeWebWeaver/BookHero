package com.parkhomovsky.bookstore.repository.role;

import com.parkhomovsky.bookstore.enums.RoleName;
import com.parkhomovsky.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role getByName(RoleName roleName);
}
