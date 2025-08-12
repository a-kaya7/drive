package net.drive.services.administration.allgemein.innensicht;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerBearbeitenService;

@Service
public class BenutzerBearbeitenService implements IBenutzerBearbeitenService{
	
	//für neues Passwort
	private String newPassowrt;
	
	private final IBenutzerRepository benutzerRepo;
	private final LogicResource logicResource;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final IMandantRepository mandantRepo;
	
	public BenutzerBearbeitenService(IBenutzerRepository benutzerRepo, LogicResource logicResource, 
			IMandantRepository mandantRepo ) {
		this.benutzerRepo = benutzerRepo;
		this.logicResource = logicResource;
		this.mandantRepo = mandantRepo;
		
	}

	@Override
	public Benutzer getBenutzerByBenutzerkennung(String benutzerkennung) {
		return benutzerRepo.findByBenutzerkennung(benutzerkennung)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("KeinBenutzer")));
	}

	@Override
	public Benutzer updateBenutzer(String benutzerkennung, Benutzer updateBenutzer) {
		Benutzer updatedBenutzer = benutzerRepo.findByBenutzerkennung(benutzerkennung)
				.orElseThrow(()-> new IllegalArgumentException(logicResource.getMessage("KeinBenutzer")));
		if(updatedBenutzer == null && benutzerkennung == null) {
			throw new IllegalArgumentException(logicResource.getMessage("KeinBenutzer"));
		}
		if(updateBenutzer.getPasswort() !=null && !updateBenutzer.getPasswort().isBlank()) {
			 newPassowrt = passwordEncoder.encode(updateBenutzer.getPasswort());
			 updatedBenutzer.setPasswort(newPassowrt);

		}
		
		updatedBenutzer.setBenutzerkennung(updateBenutzer.getBenutzerkennung());
		updatedBenutzer.setAnrede(updateBenutzer.getAnrede());
		updatedBenutzer.setVorname(updateBenutzer.getVorname());
		updatedBenutzer.setNachname(updateBenutzer.getNachname());
		updatedBenutzer.setBenutzerVon(updateBenutzer.getBenutzerVon());
		updatedBenutzer.setBenutzerBis(updateBenutzer.getBenutzerBis());
		updatedBenutzer.setPasswortAb(updateBenutzer.getPasswortAb());
		updatedBenutzer.setZeitraumPasswort(updateBenutzer.getZeitraumPasswort());
		updatedBenutzer.setPasswortAenderung(updateBenutzer.isPasswortAenderung());
		updatedBenutzer.setMfa(updateBenutzer.isMfa());
		
		String mandantIdname = updateBenutzer.getMandant().getIdname();
	    Mandant mandant = mandantRepo.findMandantByIdname(mandantIdname)
	    		.orElseThrow(()  -> new IllegalArgumentException(""));
		updatedBenutzer.setMandant(mandant);
		return benutzerRepo.save(updatedBenutzer);
	}

}
