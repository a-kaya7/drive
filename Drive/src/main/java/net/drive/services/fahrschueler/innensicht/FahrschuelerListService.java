package net.drive.services.fahrschueler.innensicht;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;

@Service
public class FahrschuelerListService implements IFahrschuelerListService{

	private final IFahrschuelerRepository fahrschuelerRepo;
	public FahrschuelerListService(IFahrschuelerRepository fahrschuelerRepo) {
		this.fahrschuelerRepo = fahrschuelerRepo;	
	}
	@Override
	@Transactional
	public List<FahrschuelerListDTO> getAllFahrschueler() {
		
		return fahrschuelerRepo.findAll().stream().map(e -> new FahrschuelerListDTO(
				e.getFahrschuelerId(),
				e.getNachname(),
				e.getGeburtsdatum(),
				e.getTelefonnummer(),
				e.getFuehrerscheine().stream().map(Fuehrerschein::getFuehrerscheinKlasse).collect(Collectors.toSet()),
				e.isBezahlt(),
				e.getMandant()
				
				)).collect(Collectors.toList());	
				
	}
}
