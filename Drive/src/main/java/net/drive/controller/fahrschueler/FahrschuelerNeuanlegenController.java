package net.drive.controller.fahrschueler;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerNeuanlegenService;


@RestController
@RequestMapping("/api")
public class FahrschuelerNeuanlegenController {

	private final IFahrschuelerNeuanlegenService fahrschueler;
	
	public FahrschuelerNeuanlegenController(IFahrschuelerNeuanlegenService fahrschueler) {
		this.fahrschueler = fahrschueler;
	}
	
	@PostMapping("/fahrschuelerneuanlage")
	public ResponseEntity<FahrschuelerDTO> createFahrschueler(@RequestBody FahrschuelerDTO fahrschuelerDto, HttpServletRequest request){
				return ResponseEntity.ok(fahrschueler.createFahrschuler(fahrschuelerDto, request));
	}
}
