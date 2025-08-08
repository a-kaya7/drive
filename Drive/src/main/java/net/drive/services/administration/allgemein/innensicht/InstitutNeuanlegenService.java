package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.InstitutDTO;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.repository.administration.allgemein.IInstitutRepository;
import net.drive.services.administration.allgemein.aussensicht.IInstitutNeuanlegenService;


@Service
public class InstitutNeuanlegenService implements IInstitutNeuanlegenService  {
	
	private final IInstitutRepository institutRepo;
	private final LogicResource logicResource;
	
	public InstitutNeuanlegenService(IInstitutRepository institutRepo, LogicResource logicResource ) {
		this.institutRepo = institutRepo;
		this.logicResource = logicResource;
	}
	

	@Override
	public InstitutDTO createInstitut(InstitutDTO institutDto) {
		
		if(institutDto.institutsname() != null && !institutDto.institutsname().isEmpty()) {
			if(institutRepo.existsByInstitutsname(institutDto.institutsname())) {
			throw new RuntimeException(logicResource.getMessage("IdnameExistiert"));
			}
		}
		Institut institutEntity = mapToEntity(institutDto);
		Institut savedEntity = institutRepo.save(institutEntity);
		return mapToDto(savedEntity);
	}
	/*
	 * Diese Methode wandelt ein InstitutDTO-Objekt in eine Institut-Entity um,
     * die dann in der Datenbank gespeichert werden kann.
     * Dabei werden alle relevanten Felder vom DTO auf die Entity übertragen
	 */
	private Institut mapToEntity(InstitutDTO institutDto) {
		Institut institutEntity = new Institut();
		institutEntity.setId(institutDto.id());
		institutEntity.setInstitutsname(institutDto.institutsname());
		institutEntity.setBezeichnung(institutDto.bezeichnung());
		institutEntity.setIban(institutDto.iban());
		institutEntity.setBic(institutDto.bic());
		institutEntity.setWaehrung(institutDto.waehrung());
		institutEntity.setLocale(institutDto.locale());
		institutEntity.setAdresse(institutDto.adresse());
		institutEntity.setTelefon(institutDto.telefon());
		institutEntity.setEmail(institutDto.email());
		return institutEntity;
	}
	
	private InstitutDTO mapToDto(Institut institut) {
	    return new InstitutDTO(
	        institut.getId(),
	        institut.getInstitutsname(),
	        institut.getBezeichnung(),
	        institut.getIban(),
	        institut.getBic(),
	        institut.getWaehrung(),
	        institut.getLocale(),
	        institut.getAdresse(),
	        institut.getTelefon(),
	        institut.getEmail()
	    );
	}


}
