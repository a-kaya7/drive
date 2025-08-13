package net.drive.model.dto.administration.allgemein;

public record PasswortWechselDTO(
		    String benutzerkennung,
		    String altesPasswort,
		    String neuesPasswort,
		    String passwortWiederholung) {

}
