package net.drive.model.entities.administration.allgemein.organisation;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import net.drive.model.entities.fahrschueler.Fahrschueler;

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
	
	@ManyToMany(mappedBy = "fuehrerscheine")
	@JsonIgnore
    private Set<Fahrschueler> fahrschueler = new HashSet<>();
	
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Fuehrerschein)) return false;
	    return fuehrerscheinId != null && fuehrerscheinId.equals(((Fuehrerschein) o).fuehrerscheinId);
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}
	
}
