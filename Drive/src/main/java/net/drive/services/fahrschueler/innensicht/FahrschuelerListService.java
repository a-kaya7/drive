package net.drive.services.fahrschueler.innensicht;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.model.entities.fahrschueler.Pruefungsstatus;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;

@Service
public class FahrschuelerListService implements IFahrschuelerListService {

    private final IFahrschuelerRepository fahrschuelerRepo;

    public FahrschuelerListService(IFahrschuelerRepository fahrschuelerRepo) {
        this.fahrschuelerRepo = fahrschuelerRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FahrschuelerListDTO> getAllFahrschueler() {
        List<Fahrschueler> entities = fahrschuelerRepo.findAll();
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toListDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FahrschuelerListDTO> getFahrschuelerBenchmark(
            String klasse,
            Integer ageMax,
            Boolean bezahlt,
            String status
    ) {
        final String klasseTrimmed = isBlank(klasse) ? null : klasse.trim();
        final String statusTrimmed = isBlank(status) ? null : status.trim();
        
        if (klasseTrimmed == null && ageMax == null && bezahlt == null && statusTrimmed == null) {
            return getAllFahrschueler();
        }
        if (klasseTrimmed != null && ageMax == null && bezahlt == null && statusTrimmed == null) {
            return mapToListDto(fahrschuelerRepo.findByFuehrerscheinKlasse(klasseTrimmed));
        }
        if (klasseTrimmed != null && ageMax != null && bezahlt == null && statusTrimmed == null) {
            LocalDate bornAfter = LocalDate.now().minusYears(ageMax.longValue() + 1L);
            return mapToListDto(fahrschuelerRepo.findByKlasseAndBornAfter(klasseTrimmed, bornAfter));
        }
        if (klasseTrimmed != null && bezahlt != null && statusTrimmed != null && ageMax == null) {
            Pruefungsstatus st = Pruefungsstatus.valueOf(statusTrimmed);
            return mapToListDto(fahrschuelerRepo.findByKlasseAndBezahltAndStatus(klasseTrimmed, bezahlt.booleanValue(), st));
        }
        return getAllFahrschueler();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private List<FahrschuelerListDTO> mapToListDto(List<Fahrschueler> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toListDto)
                .toList();
    }

    private FahrschuelerListDTO toListDto(Fahrschueler e) {
        Set<Fuehrerschein> fuehrerscheine = e.getFuehrerscheine();
        Set<String> klassen = (fuehrerscheine == null || fuehrerscheine.isEmpty())
                ? Collections.emptySet()
                : fuehrerscheine.stream()
                        .map(Fuehrerschein::getFuehrerscheinKlasse)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toUnmodifiableSet());

        return new FahrschuelerListDTO(
                e.getFahrschuelerId(),
                e.getVorname(),
                e.getNachname(),
                e.getGeburtsdatum(),
                e.getAdresse(),
                e.getTelefonnummer(),
                klassen,
                e.isBezahlt(),
                e.getPruefungsstatus(),
                e.getMandant()
        );
    }

	
}
