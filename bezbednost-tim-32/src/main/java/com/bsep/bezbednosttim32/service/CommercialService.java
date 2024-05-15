package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.model.Comercial;
import com.bsep.bezbednosttim32.repository.CommercialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommercialService {

    private final CommercialRepository commercialRepository;

    @Autowired
    public CommercialService(CommercialRepository commercialRepository) {
        this.commercialRepository = commercialRepository;
    }

    public Comercial createCommercial(Comercial commercial) {
        return commercialRepository.save(commercial);
    }
    public List<Comercial> getAllCommercials() {
        return commercialRepository.findAll();
    }

    // Dodatne metode po potrebi
}
