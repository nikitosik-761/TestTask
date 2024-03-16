package com.testApp.testApp.repositories;

import com.testApp.testApp.models.Home;
import com.testApp.testApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeRepo extends JpaRepository<Home, Long> {


    boolean existsHomeById(Long id);

    Optional<Home> findHomeByHost(User user);
}
