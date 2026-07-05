package net.drive.controller.engagement;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.services.engagement.aussensicht.IEngagementService;

@RestController
@RequestMapping("/api")
public class EngagementController {
	
	private final IEngagementService engagementService;

	
	public EngagementController (IEngagementService engagementService) {
		this.engagementService = engagementService;
	}
	
	@GetMapping("/engagement/{fahrschuelerId}")
	public ResponseEntity<Fahrschueler> getEngagement(@PathVariable("fahrschuelerId") UUID fahrschuelerId){
	return ResponseEntity.ok(engagementService.getEngagement(fahrschuelerId));
	
	}
}
