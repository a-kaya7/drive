package net.drive.services.engagement.innensicht;

import java.util.UUID;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.services.engagement.aussensicht.IEngagementService;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;


@Service
public class EngagementService implements IEngagementService {
	
	private final IFahrschuelerRepository fahrschuelerRepo;
	private final LogicResource logicResource;
	
	public EngagementService(IFahrschuelerRepository  fahrschuelerRepo, LogicResource logicResource ) {
		this.fahrschuelerRepo = fahrschuelerRepo;
		this.logicResource = logicResource;
	}

	@Override
	public Fahrschueler getEngagement(UUID fahrschulerId) {
		
		if(fahrschulerId == null) {
			throw new IllegalArgumentException(logicResource.getMessage("IDNull"));
		}
		
	  return	 fahrschuelerRepo.findById(fahrschulerId)
			  .orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("FahrschuelerNichtGefunden")));
		
		
		
	}

}
