package net.drive.controller.administration.drucken;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.drucken.aussensicht.IFahrschuelerVertragService;

@RestController
@RequestMapping("/api")
public class FahrschuelerVertragController {
	
	//Constructor Injection
	
	private final IFahrschuelerVertragService fahrschuelerVertragService;
	
	public FahrschuelerVertragController(IFahrschuelerVertragService fahrschuelerVertragService) {
		this.fahrschuelerVertragService = fahrschuelerVertragService;
	}
	 @GetMapping("/fahrschuelervertrag/{fahrschuelerId}")
	public ResponseEntity<byte[]> exportVertrag(@PathVariable ("fahrschuelerId") UUID fahrschuelerId) throws IOException{
		
		byte[] dokument = fahrschuelerVertragService.vertragErstellen(fahrschuelerId);
		
		String dateiName = "ausbildungsvertrag-template.docx";
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + dateiName)
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")).body(dokument);
	}
	

}
