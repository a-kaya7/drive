package net.drive.services.support.aussensicht;

import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.services.support.innensicht.User;

public interface IUser {
	
	public String getBenutzerkennung();
	
	public String getId();
	
	public String getVorname();
	
	public Mandant getMandant();
	
	public User getUser();
	
	public Institut getInstitut();
	
	public Benutzergruppe getBenutzergruppe();

}
