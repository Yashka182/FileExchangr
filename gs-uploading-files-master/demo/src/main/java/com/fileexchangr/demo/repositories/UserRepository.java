package com.fileexchangr.demo.repositories;

import com.fileexchangr.demo.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

