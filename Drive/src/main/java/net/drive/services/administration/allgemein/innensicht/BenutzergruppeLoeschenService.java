package net.drive.services.administration.allgemein.innensicht;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeLoeschenService;
@Service
public class BenutzergruppeLoeschenService implements IBenutzergruppeLoeschenService {

	private final IBenutzergruppeRepository bgRepo;
	private final LogicResource logicResource;
	
	public BenutzergruppeLoeschenService(IBenutzergruppeRepository bgRepo, LogicResource logicResource) {
		this.bgRepo = bgRepo;
		this.logicResource = logicResource;
	}
	@Override
	public void deleteByBenutzergruppe(String benutzergruppe) {
		Optional<Benutzergruppe> deleteBGruppe = bgRepo.findByBenutzergruppe(benutzergruppe);
		if(benutzergruppe == null && deleteBGruppe == null) {
			throw new IllegalArgumentException(logicResource.getMessage("BGruppeNichtGefunden"));
		}
		bgRepo.delete(deleteBGruppe.get());
	}

}
