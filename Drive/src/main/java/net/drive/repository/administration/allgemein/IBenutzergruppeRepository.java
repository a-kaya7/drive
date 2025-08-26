package net.drive.repository.administration.allgemein;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.model.entities.administration.allgemein.Mandant;

public interface IBenutzergruppeRepository extends JpaRepository<Benutzergruppe, UUID>{

	boolean existsByBenutzergruppe(String benutzergruppe);
	Optional<Benutzergruppe> findByBenutzergruppe(String benutzergruppe);
	List<Benutzergruppe> findByMandant(Mandant mandant);
}
