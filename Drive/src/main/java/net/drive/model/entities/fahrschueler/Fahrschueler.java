package net.drive.model.entities.fahrschueler;

import java.time.LocalDate;


import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.annotations.GenericGenerator;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import net.drive.model.datentypen.Adresse;
import net.drive.model.datentypen.AdresseConverter;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;

@Entity
@Table(name = "fahrschueler")
@Data
public class Fahrschueler {

	/*
	 *  Persönliche Daten
	 */
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID fahrschuelerId;
	
	@Column(name = "vorname")
	private String vorname;
	
	@Column(name = "nachname")
	private String nachname;
	
	@Column(name = "geburtsdatum")
	private LocalDate geburtsdatum;
	
	@Convert(converter = AdresseConverter.class)
	@Column(columnDefinition = "TEXT")
	private Adresse adresse;
	
	@Column(name = "telefonnummer")
	private String telefonnummer;
	
	@Column(name = "email")
	private String email;
	
	/*
	 * Führerscheinbezogene Daten
	 */
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
	@JoinTable(
	   name = "fahrschueler_fuehrerschein",
	   joinColumns = @JoinColumn(name = "fahrschuelerid"),
	   inverseJoinColumns = @JoinColumn(name = "fuehrerscheinid"))
       private Set<Fuehrerschein> fuehrerscheine = ConcurrentHashMap.newKeySet(); // Thread-safe


	@Column(name = "anmeldedatum")
	private LocalDate anmeldedatum;
	
	@Enumerated(EnumType.STRING)
    private Pruefungsstatus pruefungsstatus;
	
	@Column(name = "anmeldegebuehr")
	private boolean bezahlt;
	
	@Column(name = "dokumente")
	private String dokumente;
	
	@Column(name = "hinweis")
	private String hinweis;
	
	@Column(name = "notfallkontakt")
	private String notfallkontakt;
	
	@Column(name = "ersteller")
	private String ersteller;
	
	@Column(name = "mandant")
	private String mandant;
	
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Fahrschueler)) return false;
	    return fahrschuelerId != null && fahrschuelerId.equals(((Fahrschueler) o).fahrschuelerId);
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}
}
