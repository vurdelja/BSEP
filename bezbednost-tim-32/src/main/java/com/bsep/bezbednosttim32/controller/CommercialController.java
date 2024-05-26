package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.model.Commercial;
import com.bsep.bezbednosttim32.service.CommercialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bsep/commercial")
@RequiredArgsConstructor
public class CommercialController {

    private final CommercialService commercialService;

    @GetMapping("/all")
    public ResponseEntity<List<Commercial>> getCommercials() {
        List<Commercial> commercials = commercialService.getAllCommercials();
        return ResponseEntity.ok(commercials);
    }

    @PostMapping("/create")
    public ResponseEntity<Commercial> addCommercial(@RequestBody Commercial commercial) {
        Commercial newCommercial = commercialService.create(commercial);
        return ResponseEntity.ok(newCommercial);
    }
}
