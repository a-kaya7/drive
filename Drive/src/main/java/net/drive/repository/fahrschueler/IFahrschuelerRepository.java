package net.drive.repository.fahrschueler;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.model.entities.fahrschueler.Fahrschueler;



public interface IFahrschuelerRepository extends JpaRepository<Fahrschueler, UUID> {

	 boolean existsByNachnameAndGeburtsdatum( String nachname, LocalDate geburtsdatum);
	 Optional<Fahrschueler> findFarhschuelerByVorname(String vorname);
	 Optional<Fahrschueler> findByFahrschuelerId(UUID fahrschuelerId);	 
	
}
