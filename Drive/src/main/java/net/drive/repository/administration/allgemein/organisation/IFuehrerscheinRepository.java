package net.drive.repository.administration.allgemein.organisation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;

public interface IFuehrerscheinRepository extends JpaRepository<Fuehrerschein, UUID>{
 
	boolean existsByFuehrerscheinKlasse(String fuehrerscheinKlasse);
	Optional<Fuehrerschein> findByFuehrerscheinKlasse(String fuehrerscheinKlasse);
}
