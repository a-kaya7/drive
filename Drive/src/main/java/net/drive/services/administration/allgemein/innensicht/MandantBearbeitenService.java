package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IMandantBearbeitenService;

@Service
public class MandantBearbeitenService implements IMandantBearbeitenService {

	private final IMandantRepository mandantRepo;
	private final LogicResource logicResource;

	public MandantBearbeitenService(IMandantRepository mandantRepo, LogicResource logicResource) {
		this.mandantRepo = mandantRepo;
		this.logicResource = logicResource;
	}

	@Override
	public Mandant getMandantByIdname(String idname) {

		return mandantRepo.findMandantByIdname(idname)
				.orElseThrow(() -> new RuntimeException(logicResource.getMessage("MandantNichtGefunden")));
	}

	@Override
	public Mandant updateMandant(String idname, Mandant updateMandant) {
		Mandant mandant = mandantRepo.findMandantByIdname(idname).orElseThrow();

		if (idname == null || idname.isEmpty()) {
			throw new RuntimeException(logicResource.getMessage("MandantNichtGefunden"));
		}
		mandant.setIdname(updateMandant.getIdname());
		mandant.setBeschreibung(updateMandant.getBeschreibung());
		mandant.setLocale(updateMandant.getLocale());
		mandant.setTelefon(updateMandant.getTelefon());
		mandant.setAdresse(updateMandant.getAdresse());
		mandant.setEmail(updateMandant.getEmail());

		return mandantRepo.save(mandant);
	}

}
