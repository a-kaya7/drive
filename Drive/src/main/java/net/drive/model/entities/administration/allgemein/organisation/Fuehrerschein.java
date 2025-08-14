package net.drive.model.entities.administration.allgemein.organisation;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="fuehrerschein")
@Data
public class Fuehrerschein {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID fuehrerscheinId;
	
	@Column(name="fuehrerschein_klasse")
	private String fuehrerscheinKlasse;
	
	@Column(name="fahrzeuge_Ekl")
	private String fahrzeuge_Ekl;
	
	@Column(name="mindestalter")
	private int mindestalter;
	
	@Column(name="voraussetzung")
	private String voraussetzung;
	
}
