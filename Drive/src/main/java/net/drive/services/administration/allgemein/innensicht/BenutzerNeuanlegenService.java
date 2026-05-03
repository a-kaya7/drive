package net.drive.services.administration.allgemein.innensicht;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.BenutzerDTO;
import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.model.entities.administration.allgemein.Mandant;
import net.drive.repository.administration.allgemein.IBenutzerRepository;
import net.drive.repository.administration.allgemein.IBenutzergruppeRepository;
import net.drive.repository.administration.allgemein.IMandantRepository;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerNeuanlegenService;

@Service
public class BenutzerNeuanlegenService implements IBenutzerNeuanlegenService {

	private final IBenutzerRepository benutzerRepo;
	private final IBenutzergruppeRepository bgRepo;
	private final IMandantRepository mandantRepo;
	private final LogicResource logicResource;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public BenutzerNeuanlegenService(IBenutzerRepository benutzerRepo, IBenutzergruppeRepository bgRepo,
			IMandantRepository mandantRepo, LogicResource logicResource) {
		this.benutzerRepo = benutzerRepo;
		this.bgRepo = bgRepo;
		this.mandantRepo = mandantRepo;
		this.logicResource = logicResource;
	}

	@Override
	public BenutzerDTO createBenutzer(BenutzerDTO benutzerDto) {
		if (benutzerDto.benutzerkennung() != null && !benutzerDto.benutzerkennung().isEmpty()) {
			if (benutzerRepo.existsByBenutzerkennung(benutzerDto.benutzerkennung())) {
				throw new IllegalArgumentException(logicResource.getMessage("BenutzerVorhanden"));
			}
		}
		if (benutzerDto.benutzerkennung() == null) {
			throw new IllegalArgumentException(logicResource.getMessage("KeinBenutzer"));
		}

		if (benutzerDto.passwort() == null || benutzerDto.passwortWiederholung() == null
				|| !benutzerDto.passwort().equals(benutzerDto.passwortWiederholung())) {
			throw new IllegalArgumentException(logicResource.getMessage("PasswortMisMatch"));
		}

		Benutzer benutzer = mapToEntity(benutzerDto);
		Benutzer saveBenutzer = benutzerRepo.save(benutzer);
		return mapToDto(saveBenutzer);
	}

	public Benutzer mapToEntity(BenutzerDTO benutzerDto) {

		Benutzergruppe bGruppe = bgRepo.findByBenutzergruppe(benutzerDto.benutzergruppe())
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("KeinBGruppeID")));
		Mandant mandant = mandantRepo.findMandantByIdname(benutzerDto.mandant())
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("KeinMandantID")));

		Benutzer benutzer = new Benutzer();
		benutzer.setBenutzerId(benutzerDto.benutzerId());
		benutzer.setBenutzerkennung(benutzerDto.benutzerkennung());
		benutzer.setAnrede(benutzerDto.anrede());
		benutzer.setVorname(benutzerDto.vorname());
		benutzer.setNachname(benutzerDto.nachname());
		benutzer.setEmail(benutzerDto.email());
		benutzer.setBenutzerVon(benutzerDto.benutzerVon());
		benutzer.setBenutzerBis(benutzerDto.benutzerBis());
		// Passwort wird hashed
		String hashedPasswort = passwordEncoder.encode(benutzerDto.passwort());
		benutzer.setPasswort(hashedPasswort);
		benutzer.setPasswortAb(benutzerDto.passwortAb());
		benutzer.setZeitraumPasswort(benutzerDto.zeitraumPasswort());
		benutzer.setPasswortAenderung(benutzerDto.passwortAenderung());
		benutzer.setMfa(benutzerDto.mfa());
		benutzer.setBenutzergruppe(bGruppe);
		benutzer.setMandant(mandant);

		return benutzer;
	}

	public BenutzerDTO mapToDto(Benutzer benutzer) {
		return new BenutzerDTO(null,
				benutzer.getBenutzerkennung(), 
				benutzer.getAnrede(),
				benutzer.getVorname(), 
				benutzer.getNachname(), 
				benutzer.getEmail(),
				benutzer.getBenutzerVon(),
				benutzer.getBenutzerBis(), 
				null, 
				null,
				benutzer.getPasswortAb(), 
				benutzer.getZeitraumPasswort(), 
				benutzer.isPasswortAenderung(),
				benutzer.isMfa(),
				benutzer.getBenutzergruppe() != null ? benutzer.getBenutzergruppe().getBenutzergruppe() : null,
				benutzer.getMandant() != null ? benutzer.getMandant().getIdname() : null);
	}

}
