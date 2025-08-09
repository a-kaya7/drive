package net.drive.services.administration.allgemein.innensicht;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IMandantLoeschenService;

@Service
public class MandantLoeschenService implements IMandantLoeschenService {

	private final IMandantRepository mandantRepo;
	private final LogicResource logicResource;

	public MandantLoeschenService(IMandantRepository mandantRepo, LogicResource logicResource) {
		this.mandantRepo = mandantRepo;
		this.logicResource = logicResource;
	}

	@Override
	public void deleteMandantByIdname(String idname) {

		Optional<Mandant> mandant = mandantRepo.findMandantByIdname(idname);
		if (mandant == null) {
			throw new RuntimeException(logicResource.getMessage("MandantNichtGefunden"));
		}
		mandantRepo.delete(mandant.get());
	}

}
