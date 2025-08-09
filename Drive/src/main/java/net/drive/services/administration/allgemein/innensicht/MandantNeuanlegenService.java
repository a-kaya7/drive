package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.MandantDTO;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.repository.administration.allgemein.IInstitutRepository;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IMandantNeuanlegenService;

@Service
public class MandantNeuanlegenService implements IMandantNeuanlegenService {
	
	private final IInstitutRepository institutRepo;
	private final IMandantRepository mandantRepo;
	private final LogicResource logicResource;
	
	public MandantNeuanlegenService(
			IInstitutRepository institutRepo,
			IMandantRepository mandantRepo,
			LogicResource logicResource
			) {
		this.institutRepo = institutRepo;
		this.mandantRepo = mandantRepo;
		this.logicResource = logicResource;
	}
	

	@Override
	public MandantDTO createMandant(MandantDTO mandantDto) {
		if(mandantDto.idname() != null && !mandantDto.idname().isEmpty()) {
			if(mandantRepo.existsByIdname(mandantDto.idname())) {
				throw new RuntimeException(logicResource.getMessage("MandantVorhanden" + mandantDto.idname()));
			}
			
		}
		if(mandantDto.idname() == null || mandantDto.idname().isEmpty() ||
				mandantDto.institut()== null) {
			throw new RuntimeException(logicResource.getMessage("IdNameundInsitut"));
		}
		Mandant mandant = mapToEntity(mandantDto);
		Mandant saved = mandantRepo.save(mandant);
		return mapToDto(saved);
	}
	
	public Mandant mapToEntity(MandantDTO mandantDto) {
		
		Institut institut = institutRepo.findById(mandantDto.institut())
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("InstitutID" + mandantDto.institut())));
		
		Mandant mandant = new Mandant();
		mandant.setId(mandantDto.id());
		mandant.setIdname(mandantDto.idname());
		mandant.setBeschreibung(mandantDto.beschreibung());
		mandant.setInstitut(institut);
		mandant.setLocale(mandantDto.locale());
		mandant.setTelefon(mandantDto.telefon());
		mandant.setAdresse(mandantDto.adresse());
		mandant.setEmail(mandantDto.email());
		
		return mandant;
		
	}
	
	public MandantDTO mapToDto(Mandant mandant) {
		return new MandantDTO(mandant.getId(),
		mandant.getIdname(),
		mandant.getBeschreibung(),
		mandant.getInstitut() != null ? mandant.getInstitut().getId() : null,
		mandant.getLocale(),
		mandant.getTelefon(),
		mandant.getAdresse(),
		mandant.getEmail()
		);
	}

}
