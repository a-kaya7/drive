package net.drive.repository.administration.allgemein;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.drive.model.entities.administration.allgemein.Mandant;

public interface IMandantRepository extends JpaRepository<Mandant, UUID>{

	boolean existsByIdname(String idname);
}
