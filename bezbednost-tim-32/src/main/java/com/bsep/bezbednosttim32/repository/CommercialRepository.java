package com.bsep.bezbednosttim32.repository;


import com.bsep.bezbednosttim32.model.Comercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommercialRepository extends JpaRepository<Comercial, Integer> {

    List<Comercial> findAll();
}
