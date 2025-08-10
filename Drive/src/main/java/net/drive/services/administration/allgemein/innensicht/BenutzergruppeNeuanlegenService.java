package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.BenutzergruppeDTO;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeNeuanlegenService;

@Service
public class BenutzergruppeNeuanlegenService implements IBenutzergruppeNeuanlegenService {

	private final IBenutzergruppeRepository bRepo;
	private final LogicResource logicResource;

	public BenutzergruppeNeuanlegenService(IBenutzergruppeRepository bRepo, LogicResource logicResource) {
		this.bRepo = bRepo;
		this.logicResource = logicResource;
	}

	@Override
	public BenutzergruppeDTO createBenutzergruppe(BenutzergruppeDTO benutzergruppeDto) {

		if (benutzergruppeDto.benutzergruppe() != null && !benutzergruppeDto.benutzergruppe().isEmpty()) {
			if (bRepo.existsByBenutzergruppe(benutzergruppeDto.benutzergruppe())) {
			throw new IllegalArgumentException(logicResource.getMessage("BGruppeVorhanden"));
			}
		}
		if (benutzergruppeDto.benutzergruppe() == null) {
			throw new IllegalArgumentException(logicResource.getMessage("BGruppeNull"));
		}
		Benutzergruppe bGruppe = mapToEntity(benutzergruppeDto);
		Benutzergruppe savedBGruppe = bRepo.save(bGruppe);
		return mapToDto(savedBGruppe);

	}

	public Benutzergruppe mapToEntity(BenutzergruppeDTO bGruppe) {

		Benutzergruppe benutzerGruppe = new Benutzergruppe();
		benutzerGruppe.setId(bGruppe.id());
		benutzerGruppe.setBenutzergruppe(bGruppe.benutzergruppe());
		benutzerGruppe.setBeschreibung(bGruppe.beschreibung());
		benutzerGruppe.setFreigabe(bGruppe.freigabe());
		return benutzerGruppe;

	}

	public BenutzergruppeDTO mapToDto(Benutzergruppe bGruppe) {
		return new BenutzergruppeDTO(bGruppe.getId(), bGruppe.getBenutzergruppe(), bGruppe.getBeschreibung(),
				bGruppe.isFreigabe());
	}

}
