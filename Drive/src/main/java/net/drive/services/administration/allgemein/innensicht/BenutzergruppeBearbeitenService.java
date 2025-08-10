package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeBearbeitenService;

@Service
public class BenutzergruppeBearbeitenService implements IBenutzergruppeBearbeitenService {

	private final IBenutzergruppeRepository bgRepo;
	private final LogicResource logicResource;

	public BenutzergruppeBearbeitenService(IBenutzergruppeRepository bgRepo, LogicResource logicResource) {
		this.bgRepo = bgRepo;
		this.logicResource = logicResource;
	}

	@Override
	public Benutzergruppe updateBGruppe(String benutzergruppe, Benutzergruppe updateBG) {
		Benutzergruppe updateBGruppe = bgRepo.findByBenutzergruppe(benutzergruppe)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("BGruppeNichtGefunden")));
		if (updateBGruppe == null && benutzergruppe == null) {
			throw new IllegalArgumentException(logicResource.getMessage("BGruppeNichtGefunden"));
		}

		updateBGruppe.setBenutzergruppe(updateBG.getBenutzergruppe());
		updateBGruppe.setBeschreibung(updateBG.getBeschreibung());
		updateBGruppe.setFreigabe(updateBG.isFreigabe());
		return bgRepo.save(updateBGruppe);
	}

	@Override
	public Benutzergruppe getByBenutzergruppe(String benutzergruppe) {
		return bgRepo.findByBenutzergruppe(benutzergruppe)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("BGruppeNichtGefunden")));
	}

}
