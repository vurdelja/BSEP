package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.model.Commercial;
import com.bsep.bezbednosttim32.repository.CommercialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommercialService {

    private final CommercialRepository commercialRepository;

    public List<Commercial> getAllCommercials() {
        return commercialRepository.findAll();
    }

    public Commercial create(Commercial commercial) {
        return commercialRepository.save(commercial);
    }
}
