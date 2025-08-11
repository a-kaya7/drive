package net.drive.repository.administration.allgemein;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.model.dto.administration.allgemein.*;


public interface IBenutzerRepository extends JpaRepository<Benutzer, UUID> {

	boolean existsByBenutzerkennung(String benutzerkennung);
	List<Benutzer> findByBenutzergruppeId(UUID benutzergruppeId);
	Optional<Benutzer> findByBenutzerkennung(String benutzerkennung);
	
	
}
