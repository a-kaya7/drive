package net.drive.services.support.innensicht;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.Data;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.services.support.aussensicht.IUser;
@Service
@Data
public class User implements IUser {
	
	private static final Set<String> USER_SPECIFIC_KEYS = Collections.synchronizedSet(new HashSet<String>());
	
	public boolean isAdmin = false;
	public boolean fahrlehrer = false;
	
	public String ObjectId ="";
	public String benutzerId="";
	public String benutzerkennung="";
	public String vorname = "";
	public String nachname ="";
	public Mandant mandant =null;
	public Institut institut=null;
	public Benutzergruppe benutzergruppe=null;
	
	

	@Override
    public String getBenutzerkennung() {
        return benutzerkennung;
    }

    @Override
    public String getId() {
        return benutzerId;
    }

    @Override
    public String getVorname() {
        return vorname;
    }

    @Override
    public Mandant getMandant() {
        return mandant;
    }

    @Override
    public User getUser() {
        return this;
    }

    @Override
    public Institut getInstitut() {
        return institut;
    }

    @Override
    public Benutzergruppe getBenutzergruppe() {
        return benutzergruppe;
    }


}
