package net.drive.model.entities.administration.allgemein;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import net.drive.model.datentypen.Adresse;
import net.drive.model.datentypen.AdresseConverter;

@Entity
@Table(name ="mandanten")
@Data
public class Mandant {
	
	
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "mandant_name")
	private String idname;
	
	@Column(name = "beschreibung")
	private String beschreibung;
	
	@ManyToOne
	@JoinColumn(name = "institut")
	private Institut institut;
	
	@Column(name = "local")
	private String locale;
	
	@Column(name = "telefon")
	private String telefon;
	
	@Convert(converter = AdresseConverter.class)
	@Column(columnDefinition = "TEXT")
	private Adresse adresse;
	
	@Column(name = "email")
	private String email;
	
	
	

}
