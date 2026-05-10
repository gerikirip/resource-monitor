package com.gerikirip.resource_monitor.server.repository;

import com.gerikirip.resource_monitor.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    boolean existsByName(String name);

    Optional<Server> findByName(String name);
}
