package com.starkindustries.Spring_Security1.auth.repository;

import com.starkindustries.Spring_Security1.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {

    public Users findByUsername(String username);
}
