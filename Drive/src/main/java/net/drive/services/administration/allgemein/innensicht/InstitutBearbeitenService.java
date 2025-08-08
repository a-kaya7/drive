package net.drive.services.administration.allgemein.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.repository.administration.allgemein.IInstitutRepository;
import net.drive.services.administration.allgemein.aussensicht.IInsitutBearbeitenService;

@Service
public class InstitutBearbeitenService implements IInsitutBearbeitenService {

	
	private final IInstitutRepository institutRepo;
	private final LogicResource logicResource;
	
	public InstitutBearbeitenService(IInstitutRepository institutRepo, LogicResource logicResource) {
		this.institutRepo = institutRepo;
		this.logicResource = logicResource;
	}
	@Override
	public Institut updateInsitut(String institutsname, Institut updateData) {
		Institut update = institutRepo.findByInstitutsname(institutsname)
				.orElseThrow(() -> new RuntimeException("Institutsname wurde nicht gefunden"));
    if(institutsname == null || institutsname.isEmpty()) {
    	throw new IllegalArgumentException(logicResource.getMessage("InstitutNull"));
    } else {
    	
    	update.setInstitutsname(updateData.getInstitutsname());
    	update.setBezeichnung(updateData.getBezeichnung());
    	update.setIban(updateData.getIban());
    	update.setBic(updateData.getBic());
    	update.setLocale(updateData.getLocale());
    	update.setAdresse(updateData.getAdresse());
    	update.setTelefon(updateData.getTelefon());
    	update.setEmail(updateData.getEmail());
    	return institutRepo.save(update);
    	
    }
		
		
	}

	@Override
	public Institut getInstitutByInstitutsname(String institutsname) {
		
		return institutRepo.findByInstitutsname(institutsname)
				.orElseThrow(() -> new RuntimeException(logicResource.getMessage("InstitutNichtGefunden")));
	}

}
