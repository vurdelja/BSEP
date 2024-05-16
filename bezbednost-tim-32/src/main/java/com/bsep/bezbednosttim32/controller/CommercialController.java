package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.model.Comercial;
import com.bsep.bezbednosttim32.service.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bsep/auth")
public class CommercialController {

    private final CommercialService commercialService;

    @Autowired
    public CommercialController(CommercialService commercialService) {
        this.commercialService = commercialService;
    }

    @PostMapping("/commercial")
    public ResponseEntity<Comercial> createCommercial(@RequestBody Comercial commercial) {
        Comercial createdCommercial = commercialService.createCommercial(commercial);
        return new ResponseEntity<>(createdCommercial, HttpStatus.CREATED);
    }
    @GetMapping("/commercial")
    public ResponseEntity<List<Comercial>> getAllCommercials() {
        List<Comercial> commercials = commercialService.getAllCommercials();
        return new ResponseEntity<>(commercials, HttpStatus.OK);
    }

    // Dodatne metode po potrebi
}
