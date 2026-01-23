package net.drive.controller.fahrschueler;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;

@RestController
@RequestMapping("/api")
public class FahrschuelerListServiceController {

    private final IFahrschuelerListService fahrschuelerService;

    public FahrschuelerListServiceController(IFahrschuelerListService fahrschuelerService) {
        this.fahrschuelerService = fahrschuelerService;
    }

    @GetMapping("/fahrschuelerlist")
    public ResponseEntity<List<FahrschuelerListDTO>> getFarhschueler(
            @RequestParam(name = "klasse", required = false) String klasse,
            @RequestParam(name = "ageMax", required = false) Integer ageMax,
            @RequestParam(name = "bezahlt", required = false) Boolean bezahlt,
            @RequestParam(name = "status", required = false) String status
    ) {
        return ResponseEntity.ok(
                fahrschuelerService.getFahrschuelerBenchmark(klasse, ageMax, bezahlt, status)
        );
    }
}
