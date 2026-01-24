package net.drive.services.fahrschueler.innensicht;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
    @Transactional
    public List<FahrschuelerListDTO> getAllFahrschueler() {
        List<Fahrschueler> entities = fahrschuelerRepo.findAll();
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDto)
                .toList();
    }

    @Override
    @Transactional
    public List<FahrschuelerListDTO> getFahrschuelerBenchmark(
            String klasse,
            Integer ageMax,
            Boolean bezahlt,
            String status
    ) {
        // Fal 1:  (20.000)
        if (isBlank(klasse) && ageMax == null && bezahlt == null && isBlank(status)) {
            return getAllFahrschueler();
        }

        // Fall 2: sadece Klasse B
        if (!isBlank(klasse) && ageMax == null && bezahlt == null && isBlank(status)) {
            return fahrschuelerRepo.findByFuehrerscheinKlasse(klasse.trim()).stream()
                    .filter(Objects::nonNull)
                    .map(this::toListDto)
                    .toList();
        }

        // Fall 3: Klasse B + 30 Alter
        if (!isBlank(klasse) && ageMax != null && bezahlt == null && isBlank(status)) {
            // ageMax=29 => unter 30 
            LocalDate bornAfter = LocalDate.now().minusYears(ageMax.longValue() + 1L);
            return fahrschuelerRepo.findByKlasseAndBornAfter(klasse.trim(), bornAfter).stream()
                    .filter(Objects::nonNull)
                    .map(this::toListDto)
                    .toList();
        }

        // Fall 4: Klasse B + bezahlt + pruefungsstatus
        if (!isBlank(klasse) && bezahlt != null && !isBlank(status) && ageMax == null) {
            Pruefungsstatus st = Pruefungsstatus.valueOf(status.trim());
            return fahrschuelerRepo.findByKlasseAndBezahltAndStatus(
                            klasse.trim(),
                            bezahlt.booleanValue(),
                            st
                    )
                    .stream()
                    .filter(Objects::nonNull)
                    .map(this::toListDto)
                    .toList();
        }

        // Fallback:
        return getAllFahrschueler();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private FahrschuelerListDTO toListDto(Fahrschueler e) {

        Set<String> klassen;
        if (e.getFuehrerscheine() == null || e.getFuehrerscheine().isEmpty()) {
            klassen = Collections.emptySet();
        } else {
            klassen = e.getFuehrerscheine().stream()
                    .filter(Objects::nonNull)
                    .map(Fuehrerschein::getFuehrerscheinKlasse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableSet());
        }

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
