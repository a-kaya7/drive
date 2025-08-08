package net.drive.controller.administration.allgemein;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.InstitutListDTO;
import net.drive.services.administration.allgemein.aussensicht.IInstitutListService;

@RestController
@RequestMapping("/api")
public class InstitutListController {

	private final IInstitutListService institutList;
	
	public InstitutListController(IInstitutListService institutList) {
		this.institutList = institutList;
	}
	
	
	@GetMapping("/institutlist")
	public ResponseEntity<List<InstitutListDTO>>  getAllInstitute(){
		return ResponseEntity.ok(institutList.getAllInstitut());
	}
}
