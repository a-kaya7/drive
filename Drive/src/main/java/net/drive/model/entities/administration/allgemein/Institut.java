package net.drive.model.entities.administration.allgemein;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import net.drive.model.datentypen.Adresse;
import net.drive.model.datentypen.AdresseConverter;

@Entity
@Table(name="instituten")
@Data
public class Institut {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "institutsname")
    private String institutsname;

    @Column(name = "bezeichnung")
    private String bezeichnung;
    
    @Column(name = "iban")
    private String iban;
    
    @Column(name = "bic")
    private String bic;
    
    @Column(name = "waehrung")
    private String waehrung;

    @Column(name = "locale")
    private String locale;
    
    @Convert(converter = AdresseConverter.class)
	@Column(columnDefinition = "TEXT")
    private Adresse adresse;
    
    @Column(name = "telefon")
    private String telefon;
    
    @Column(name = "email")
    private String email;

}
