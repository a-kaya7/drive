package net.drive.services.administration.allgemein.innensicht;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.InstitutListDTO;
import net.drive.repository.administration.allgemein.IInstitutRepository;
import net.drive.services.administration.allgemein.aussensicht.IInstitutListService;

@Service
public class InstitutListService implements IInstitutListService {
	
	private final IInstitutRepository institutRepo;
	
	public InstitutListService(IInstitutRepository institutRepo) {
		this.institutRepo = institutRepo;
	}

	@Override
	public List<InstitutListDTO> getAllInstitut() {
	
		return institutRepo.findAll().stream().map(entity -> new InstitutListDTO(
				entity.getId(),
				entity.getInstitutsname(),
				entity.getBezeichnung(),
				entity.getLocale(),
				entity.getTelefon()
				)).collect(Collectors.toList());
	}

}
