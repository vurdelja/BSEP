package com.bsep.bezbednosttim32.repository;

import com.bsep.bezbednosttim32.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Optional<Request> findRequestById(Integer id);
    List<Request> findByEmail(String email);


    Optional<Request> findByActivationToken(String activationToken);
}
