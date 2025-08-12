package net.drive.services.administration.allgemein.innensicht;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.BenutzerListDTO;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerListService;
@Service
public class BenutzerListService implements IBenutzerListService {

	private final IBenutzerRepository benutzerRepo;
	
	public BenutzerListService(IBenutzerRepository benutzerRepo) {
		this.benutzerRepo = benutzerRepo;
	}
	@Override
	public List<BenutzerListDTO> getAllBenutzer() {
		
		return benutzerRepo.findAll().stream().map(e -> new BenutzerListDTO(
				e.getId(),
				e.getBenutzerkennung(),
				e.getVorname(),
				e.getBenutzerBis(),
				e.getBenutzergruppe().getBenutzergruppe(),
				e.getMandant().getIdname()
				)).collect(Collectors.toList());
	}

}
