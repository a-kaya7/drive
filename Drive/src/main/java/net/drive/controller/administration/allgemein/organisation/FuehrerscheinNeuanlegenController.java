package net.drive.controller.administration.allgemein.organisation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import net.drive.services.administration.allgemein.organisation.aussensicht.IFuehrerscheinNeuanlegenService;
import net.drive.services.support.innensicht.SGlue;
import net.drive.services.support.innensicht.User;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;

@RestController
@RequestMapping("/api")
public class FuehrerscheinNeuanlegenController {

	private final IFuehrerscheinNeuanlegenService fuehrerschein;

	public FuehrerscheinNeuanlegenController(IFuehrerscheinNeuanlegenService fuehrerschein) {
		this.fuehrerschein = fuehrerschein;
	}

	User DUser = SGlue.GetUser();
	@PostMapping("fuehrerscheinneuanlage")
	public ResponseEntity<FuehrerscheinDTO> createFuehrerschein(@RequestBody FuehrerscheinDTO fuehrerscheinDto) {
	if (DUser.getBenutzergruppe().getBenutzergruppe().equals("ADMIN")) {
		throw new IllegalArgumentException("");
		}
		return ResponseEntity.ok(fuehrerschein.createFeuhrerschein(fuehrerscheinDto));
	}
}
