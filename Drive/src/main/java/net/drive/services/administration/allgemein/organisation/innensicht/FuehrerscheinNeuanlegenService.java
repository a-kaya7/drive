package net.drive.services.administration.allgemein.organisation.innensicht;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.services.administration.allgemein.organisation.aussensicht.IFuehrerscheinNeuanlegenService;
import net.drive.services.support.innensicht.SGlue;
import net.drive.services.support.innensicht.User;

@Service
public class FuehrerscheinNeuanlegenService  implements IFuehrerscheinNeuanlegenService {

	
	private final IFuehrerscheinRepository fuehrerscheinRepo;
	private final LogicResource logicResource;
	

	
	public FuehrerscheinNeuanlegenService(IFuehrerscheinRepository fuehrerscheinRepo,LogicResource logicResource ) {
		this.fuehrerscheinRepo = fuehrerscheinRepo;
		this.logicResource = logicResource;
	}

	@Override
	public FuehrerscheinDTO createFeuhrerschein(FuehrerscheinDTO fuehrerscheinDto) {
		if(fuehrerscheinDto.fuehrerscheinKlasse() != null && !fuehrerscheinDto.fuehrerscheinKlasse().isEmpty()) {
			if(fuehrerscheinRepo.existsByFuehrerscheinKlasse(fuehrerscheinDto.fuehrerscheinKlasse())){
				throw new RuntimeException(logicResource.getMessage("FuehrerscheinVorhanden"));
			}
		}
		if(fuehrerscheinDto.fuehrerscheinKlasse() ==null && fuehrerscheinDto.fuehrerscheinKlasse().isEmpty()) {
			throw new RuntimeException(logicResource.getMessage("KeinFuehrerschein"));
		}
		
		Fuehrerschein fuehrerschein = mapToEntity(fuehrerscheinDto);
		Fuehrerschein saved = fuehrerscheinRepo.save(fuehrerschein);
		return mapToDto(saved);
	}
	
	public Fuehrerschein mapToEntity(FuehrerscheinDTO  fuehrerscheinDto) {
		
		Fuehrerschein fuehrerschein = new Fuehrerschein();
		fuehrerschein.setFuehrerscheinId(fuehrerscheinDto.fuehrerscheinId());
		fuehrerschein.setFuehrerscheinKlasse(fuehrerscheinDto.fuehrerscheinKlasse());
		fuehrerschein.setFahrzeuge_Ekl(fuehrerscheinDto.fahrzeuge_Ekl());
		fuehrerschein.setMindestalter(fuehrerscheinDto.mindestalter());
		fuehrerschein.setVoraussetzung(fuehrerscheinDto.voraussetzung());
		return fuehrerschein;
	}
	
	public FuehrerscheinDTO mapToDto(Fuehrerschein fuehrerschein) {
		return new FuehrerscheinDTO(
				fuehrerschein.getFuehrerscheinId(),
				fuehrerschein.getFuehrerscheinKlasse(),
				fuehrerschein.getFahrzeuge_Ekl(),
				fuehrerschein.getMindestalter(),
				fuehrerschein.getVoraussetzung()
				);
	}
	
}
