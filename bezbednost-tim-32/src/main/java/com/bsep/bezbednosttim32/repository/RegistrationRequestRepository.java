package com.bsep.bezbednosttim32.repository;

import com.bsep.bezbednosttim32.model.RegistrationRequest;
import com.bsep.bezbednosttim32.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

}
