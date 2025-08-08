package net.drive.repository.administration.allgemein;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.entities.administration.allgemein.Institut;

public interface IInstitutRepository extends JpaRepository<Institut, UUID> {

	boolean existsByInstitutsname(String institutsname);
	Optional<Institut> findByInstitutsname(String institutsname);
}
