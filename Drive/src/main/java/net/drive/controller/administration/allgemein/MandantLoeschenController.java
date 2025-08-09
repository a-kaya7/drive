package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IMandantLoeschenService;

@RestController
@RequestMapping("/api")
public class MandantLoeschenController {

	private final IMandantLoeschenService mandantService;

	public MandantLoeschenController(IMandantLoeschenService mandantService) {
		this.mandantService = mandantService;
	}

	@DeleteMapping("/mandant/{idname}")
	public ResponseEntity<?> deleteMandant(@PathVariable("idname") String idname) {
		mandantService.deleteMandantByIdname(idname);
		return ResponseEntity.ok().body(idname);
	}

}
