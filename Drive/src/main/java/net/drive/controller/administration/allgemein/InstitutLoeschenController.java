package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.config.LogicResource;
import net.drive.services.administration.allgemein.aussensicht.IInstitutLoeschenService;

@RestController
@RequestMapping("/api")
public class InstitutLoeschenController {

	private final IInstitutLoeschenService institutService;
	private final LogicResource logicResource;
	
	public InstitutLoeschenController(IInstitutLoeschenService institutService, LogicResource logicResource) {
		this.institutService = institutService;
		this.logicResource = logicResource;
	}
	
	@DeleteMapping("/institut/{institutsname}")
	public ResponseEntity<?> deleteInstitut(@PathVariable("institutsname") String institutsname){
		institutService.deleteInstitutByInstitutsname(institutsname);
		return ResponseEntity.ok().body(logicResource.getMessage("InstitutLoesch") + institutsname);
	}
}
