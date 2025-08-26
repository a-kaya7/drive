package net.drive.services.fahrschueler.innensicht;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerLoeschenService;

@Service
public class FahrschuelerLoeschenService implements IFahrschuelerLoeschenService {
 
	private final IFahrschuelerRepository fahrschuelerRepo;
	
	public FahrschuelerLoeschenService(IFahrschuelerRepository fahrschuelerRepo ) {
		this.fahrschuelerRepo =  fahrschuelerRepo;
	}
	@Override
	public void deleteFahrschueler(UUID fahrschuelerId) {
		
		Optional<Fahrschueler> fhSchueler = fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId);
		if(fhSchueler == null && fahrschuelerId == null ) {
			throw new IllegalArgumentException("KeinFahrschüler");
		}
		fahrschuelerRepo.delete(fhSchueler.get());
	}

}
