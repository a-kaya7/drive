package net.drive.services.administration.allgemein.innensicht;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.BenutzergruppeDTO;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeListService;
@Service
public class BenutzergruppeListService implements IBenutzergruppeListService {

	private final IBenutzergruppeRepository bgRepo;
	
	public BenutzergruppeListService(IBenutzergruppeRepository bgRepo) {
		this.bgRepo = bgRepo;
		
	}
	@Override
	public List<BenutzergruppeDTO> getAllBenutzergruppe() {
		
		return bgRepo.findAll().stream().map(bg -> new BenutzergruppeDTO(
				bg.getId(),
				bg.getBenutzergruppe(),
				bg.getBeschreibung(),
				bg.isFreigabe(),
				bg.getMandant().getIdname()
				)).collect(Collectors.toList());
	}

}
