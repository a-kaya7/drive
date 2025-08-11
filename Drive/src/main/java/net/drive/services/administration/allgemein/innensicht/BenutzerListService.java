package net.drive.services.administration.allgemein.innensicht;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.drive.model.dto.administration.allgemein.BenutzerListDTO;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerListService;
@Service
public class BenutzerListService implements IBenutzerListService {

	private final IBenutzerRepository benutzerRepo;
	private final IBenutzergruppeRepository bgRepo;
	
	public BenutzerListService(IBenutzerRepository benutzerRepo, IBenutzergruppeRepository bgRepo) {
		this.benutzerRepo = benutzerRepo;
		this.bgRepo = bgRepo;
	}
	@Override
	public List<BenutzerListDTO> getAllBenutzer() {
		
		return benutzerRepo.findAll().stream().map(e -> new BenutzerListDTO(
				e.getId(),
				e.getBenutzerkennung(),
				e.getVorname(),
				e.getBenutzerBis(),
				e.getBenutzergruppe().getBenutzergruppe()
				)).collect(Collectors.toList());
	}

}
