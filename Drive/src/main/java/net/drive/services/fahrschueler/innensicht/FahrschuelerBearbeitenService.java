package net.drive.services.fahrschueler.innensicht;

import java.util.UUID;


import org.springframework.stereotype.Service;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerBearbeitenService;

@Service
public class FahrschuelerBearbeitenService implements IFahrschuelerBearbeitenService {
	
	private final IFahrschuelerRepository fahrschuelerRepo;
	private final IFuehrerscheinRepository fuehrerscheinRepo;
	
	public FahrschuelerBearbeitenService(IFahrschuelerRepository fahrschuelerRepo,
			IFuehrerscheinRepository fuehrerscheinRepo) {
		this.fahrschuelerRepo = fahrschuelerRepo;
		this.fuehrerscheinRepo = fuehrerscheinRepo;
	}

	@Override
	public Fahrschueler getFahrschueler(UUID fahrschuelerId) {
		return fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId).orElseThrow(()
				-> new  IllegalArgumentException());
	}

	@Override
	public Fahrschueler updateFahrschueler(Fahrschueler udFahrschueler, UUID fahrschuelerId) {
		Fahrschueler updatedFahrschueler = fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId)
				.orElseThrow(() -> new IllegalArgumentException());
		
		if(updatedFahrschueler == null && fahrschuelerId == null) {
		      throw  new IllegalArgumentException("");
		}
		updatedFahrschueler.setVorname(udFahrschueler.getVorname());
		updatedFahrschueler.setNachname(udFahrschueler.getNachname());
		updatedFahrschueler.setGeburtsdatum(udFahrschueler.getGeburtsdatum());
		updatedFahrschueler.setAdresse(udFahrschueler.getAdresse());
		updatedFahrschueler.setTelefonnummer(udFahrschueler.getTelefonnummer());
		updatedFahrschueler.setEmail(udFahrschueler.getEmail());
		updatedFahrschueler.setAnmeldedatum(udFahrschueler.getAnmeldedatum());
		updatedFahrschueler.setBezahlt(udFahrschueler.isBezahlt());
		updatedFahrschueler.setDokumente(udFahrschueler.getDokumente());
		updatedFahrschueler.setHinweis(udFahrschueler.getHinweis());
		updatedFahrschueler.setNotfallkontakt(udFahrschueler.getNotfallkontakt());
		
		
		if(udFahrschueler.getFuehrerscheine() != null) {
			updatedFahrschueler.getFuehrerscheine().forEach(f -> f.getFahrschueler().remove(updatedFahrschueler));
			updatedFahrschueler.getFuehrerscheine().clear();
			
			udFahrschueler.getFuehrerscheine().forEach(f -> { 
				
			fuehrerscheinRepo.findByFuehrerscheinId(f.getFuehrerscheinId()).ifPresent(fuehrerschein -> {
				updatedFahrschueler.getFuehrerscheine().add(fuehrerschein);
				fuehrerschein.getFahrschueler().add(updatedFahrschueler);
			});
			});
			
			
		}
		return fahrschuelerRepo.save(updatedFahrschueler);
	}

}
