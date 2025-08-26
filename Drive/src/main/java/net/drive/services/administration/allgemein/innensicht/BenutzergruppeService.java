package net.drive.services.administration.allgemein.innensicht;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;

@Service
@AllArgsConstructor
public class BenutzergruppeService {

	private final IBenutzergruppeRepository bRepo;
	
	public List<Benutzergruppe> getBGruppe(Benutzer benutzer){
		return bRepo.findByMandant(benutzer.getMandant());
	}
}
