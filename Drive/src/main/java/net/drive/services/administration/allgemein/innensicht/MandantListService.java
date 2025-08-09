package net.drive.services.administration.allgemein.innensicht;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.MandantListDTO;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IMandantListService;
@Service
public class MandantListService implements IMandantListService {

	private final IMandantRepository mandantRepo;
	public MandantListService(IMandantRepository mandantRepo) {
		this.mandantRepo = mandantRepo;	
	}
	
	@Override
	public List<MandantListDTO> getAllMandant() {
		return mandantRepo.findAll().stream().map(e -> new MandantListDTO(
				e.getId(),
				e.getIdname(),
				e.getBeschreibung(), 
				e.getLocale(), 
				e.getTelefon())).collect(Collectors.toList());
	}

}
