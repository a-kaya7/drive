package net.drive.services.administration.allgemein.organisation.innensicht;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.services.administration.allgemein.organisation.aussensicht.IFuehrerscheinListService;

@Service
public class FuehrerscheinListService implements IFuehrerscheinListService {

	private final IFuehrerscheinRepository fuehrerscheinRepo;
	public FuehrerscheinListService( IFuehrerscheinRepository fuehrerscheinRepo) {
		this.fuehrerscheinRepo = fuehrerscheinRepo;
		
	}
	@Override
	public List<FuehrerscheinDTO> getAllFuehrerschein() {
		
		return fuehrerscheinRepo.findAll().stream().map(f -> new FuehrerscheinDTO(
				f.getFuehrerscheinId(),
				f.getFuehrerscheinKlasse(),
				f.getFahrzeuge_Ekl(),
				f.getMindestalter(),
				f.getVoraussetzung()
				)).collect(Collectors.toList());
	}

}
