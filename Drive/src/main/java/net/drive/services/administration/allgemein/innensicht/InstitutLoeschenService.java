package net.drive.services.administration.allgemein.innensicht;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.repository.administration.allgemein.IInstitutRepository;
import net.drive.services.administration.allgemein.aussensicht.IInstitutLoeschenService;

@Service
public class InstitutLoeschenService implements IInstitutLoeschenService{

	private final IInstitutRepository institutRepo;
	private final LogicResource logicResource;
	public InstitutLoeschenService(IInstitutRepository institutRepo, LogicResource logicResource) {
		this.institutRepo = institutRepo;
		this.logicResource = logicResource;
	}
	
	
	@Override
	public void deleteInstitutByInstitutsname(String institutsname) {
		
		Optional<Institut> opInstitut = institutRepo.findByInstitutsname(institutsname);
		
		if(opInstitut == null) {
			throw new RuntimeException(logicResource.getMessage("KeinInstitut"));
		}
		institutRepo.delete(opInstitut.get());
	}

}
