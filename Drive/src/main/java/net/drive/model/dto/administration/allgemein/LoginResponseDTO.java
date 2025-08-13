package net.drive.model.dto.administration.allgemein;

public record LoginResponseDTO(
		String benutzerkennung,
	    boolean passwortAenderung
	  ) {

}
