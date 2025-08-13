package net.drive.services.administration.allgemein.innensicht;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerLoeschenService;

@Service
public class BenutzerLoeschenService implements IBenutzerLoeschenService {

	private final IBenutzerRepository benutzerRepo;
	private final LogicResource logicResource;
	public BenutzerLoeschenService(IBenutzerRepository benutzerRepo, LogicResource logicResource ) {
		this.benutzerRepo = benutzerRepo;
		this.logicResource = logicResource;
	}
	
	@Override
	public void deleteBenutzer(String benutzerkennung) {
		Optional<Benutzer> benutzer = benutzerRepo.findByBenutzerkennung(benutzerkennung);
		if(benutzerkennung == null && benutzer == null) {
			throw new IllegalArgumentException(logicResource.getMessage("KeinBenutzer"));
		} else {
			benutzerRepo.delete(benutzer.get());
		}
	}

}
