package net.drive.services.fahrschueler.innensicht;

import org.hibernate.Hibernate;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.drive.config.JwtService;
import net.drive.config.LogicResource;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;
import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerNeuanlegenService;

@Service

public class FahrschuelerNeuanlegenService implements IFahrschuelerNeuanlegenService {

    private final IFahrschuelerRepository fahrschuelerRepo;
    private final LogicResource logicResource;
    private final IFuehrerscheinRepository fuehrerscheinRepo;
    private final JwtService  jwtService;
    

    public FahrschuelerNeuanlegenService(
    		IFahrschuelerRepository fahrschuelerRepo,
    		LogicResource logicResource,
    		IFuehrerscheinRepository fuehrerscheinRepo,
    		JwtService  jwtService) {
        this.fahrschuelerRepo = fahrschuelerRepo;
        this.logicResource = logicResource;
        this.fuehrerscheinRepo = fuehrerscheinRepo;
        this.jwtService = jwtService;
  
    }
    
    private String benutzerkennung ="";
    
    @Override
    @Transactional
    public FahrschuelerDTO createFahrschuler(FahrschuelerDTO fahrschuelerDto, HttpServletRequest  request) {
        validateInput(fahrschuelerDto);
 
        Fahrschueler entity = mapToEntity(fahrschuelerDto);
        Set<Fuehrerschein> fuehrerscheinSet = loadFuehrerscheine(fahrschuelerDto);
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	String token = authHeader.replace("Bearer ", "");
        	benutzerkennung = jwtService.getBenutzerkennungFromToken(token);
        }
        entity.setErsteller(benutzerkennung);

        entity.setFuehrerscheine(fuehrerscheinSet);
        Fahrschueler saved = fahrschuelerRepo.save(entity);
        return mapToDto(saved);
    }

    private void validateInput(FahrschuelerDTO dto) {
        if (dto.vorname() != null && !dto.vorname().isEmpty()) {
            if (fahrschuelerRepo.existsByNachnameAndGeburtsdatum(dto.nachname(), dto.geburtsdatum())) {
                throw new IllegalArgumentException(logicResource.getMessage("FahrschuelerVorhanden"));
            }
        }
        
        if (dto.vorname() == null || dto.nachname() == null || dto.geburtsdatum() == null) {
            throw new IllegalArgumentException(logicResource.getMessage("VorUndNachnameDatum"));
        }
    }

    private Set<Fuehrerschein> loadFuehrerscheine(FahrschuelerDTO dto) {
        Set<Fuehrerschein> fuehrerscheinSet = ConcurrentHashMap.newKeySet(); // Thread-safe 
        
        if (dto.fuehrerscheine() != null) {
            dto.fuehrerscheine().parallelStream().forEach(fDto -> {
                fuehrerscheinRepo.findByFuehrerscheinId(fDto.fuehrerscheinId())
                    .ifPresent(fuehrerschein -> {
                        fuehrerscheinSet.add(fuehrerschein);
                        
                        fuehrerschein.getFahrschueler().add(mapToEntity(dto));
                    });
            });
        }
        return fuehrerscheinSet;
    }

    public Fahrschueler mapToEntity(FahrschuelerDTO dto) { 
        Fahrschueler entity = new Fahrschueler();
        entity.setVorname(dto.vorname());
        entity.setNachname(dto.nachname());
        entity.setGeburtsdatum(dto.geburtsdatum());
        entity.setAdresse(dto.adresse());
        entity.setTelefonnummer(dto.telefonnummer());
        entity.setEmail(dto.email());
        entity.setAnmeldedatum(dto.anmeldedatum());
        entity.setPruefungsstatus(dto.pruefungsstatus());
        entity.setBezahlt(dto.bezahlt());
        entity.setDokumente(dto.dokumente());
        entity.setHinweis(dto.hinweis());
        entity.setNotfallkontakt(dto.notfallkontakt());
 
        
        return entity;
    }

    public FahrschuelerDTO mapToDto(Fahrschueler entity) {
        
        if (entity.getFuehrerscheine() != null) {
            Hibernate.initialize(entity.getFuehrerscheine());
        }

        Set<FuehrerscheinDTO> fuehrerscheinDtoSet = entity.getFuehrerscheine() == null ?
            ConcurrentHashMap.newKeySet() :
            entity.getFuehrerscheine().parallelStream()
                .map(f -> new FuehrerscheinDTO(
                    f.getFuehrerscheinId(),
                    f.getFuehrerscheinKlasse(),
                    null,
                    0,
                    null
                ))
                .collect(Collectors.toSet());

        return new FahrschuelerDTO(
            entity.getFahrschuelerId(),
            entity.getVorname(),
            entity.getNachname(),
            entity.getGeburtsdatum(),
            entity.getAdresse(),
            entity.getTelefonnummer(),
            entity.getEmail(),
            fuehrerscheinDtoSet,
            entity.getAnmeldedatum(),
            entity.getPruefungsstatus(),
            entity.isBezahlt(),
            entity.getDokumente(),
            entity.getHinweis(),
            entity.getNotfallkontakt(),
            entity.getErsteller()
        );
    }
    
}