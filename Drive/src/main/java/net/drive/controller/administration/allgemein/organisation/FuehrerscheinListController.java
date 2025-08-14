package net.drive.controller.administration.allgemein.organisation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.organisation.aussensicht.IFuehrerscheinListService;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;

@RestController
@RequestMapping("/api")
public class FuehrerscheinListController {

	private final IFuehrerscheinListService fuehrerscheinService;

	public FuehrerscheinListController(IFuehrerscheinListService fuehrerscheinService) {
		this.fuehrerscheinService = fuehrerscheinService;

	}

	@GetMapping("/fuehrerscheinlist")
	public ResponseEntity<List<FuehrerscheinDTO>> getAllFuehrerschein() {
		return ResponseEntity.ok(fuehrerscheinService.getAllFuehrerschein());
	}
}
