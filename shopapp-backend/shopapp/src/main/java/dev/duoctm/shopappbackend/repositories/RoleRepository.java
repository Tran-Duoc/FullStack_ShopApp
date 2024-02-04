package dev.duoctm.shopappbackend.repositories;

import dev.duoctm.shopappbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
