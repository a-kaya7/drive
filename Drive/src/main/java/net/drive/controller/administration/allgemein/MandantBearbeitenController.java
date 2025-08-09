package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.services.administration.allgemein.aussensicht.IMandantBearbeitenService;

@RestController
@RequestMapping("/api")
public class MandantBearbeitenController {

	private final IMandantBearbeitenService mandantService;

	public MandantBearbeitenController(IMandantBearbeitenService mandantService) {
		this.mandantService = mandantService;
	}

	@GetMapping("mandant/{idname}")
	public ResponseEntity<Mandant> getMandant(@PathVariable("idname") String idname) {
		Mandant mandant = mandantService.getMandantByIdname(idname);
		return ResponseEntity.ok(mandant);
	}

	@PutMapping("mandantbearbeiten/{idname}")
	public ResponseEntity<Mandant> updateMandant(@PathVariable("idname") String idname,
			@RequestBody Mandant updatedMandant) {

		Mandant mandant = mandantService.updateMandant(idname, updatedMandant);
		return ResponseEntity.ok(mandant);
	}
}
