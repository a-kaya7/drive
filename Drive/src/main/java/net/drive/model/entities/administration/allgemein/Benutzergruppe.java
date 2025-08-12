package net.drive.model.entities.administration.allgemein;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="benutzergruppen")
@Data
public class Benutzergruppe {

	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "benutzergruppe")
	private String benutzergruppe;
	
	@Column(name = "beschreibung")
	private String beschreibung;
	
	@Column(name = "freigabe")
	private boolean freigabe;
	
	@ManyToOne
	@JoinColumn(name = "mandant")
	private Mandant mandant;
	
}
