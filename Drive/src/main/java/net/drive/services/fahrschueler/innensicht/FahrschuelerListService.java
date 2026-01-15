package net.drive.services.fahrschueler.innensicht;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;

/**
 * Service-Klasse zur Bereitstellung einer Liste von Fahrschülern (Innensicht).
 * <p>
 * Diese Klasse lädt alle {@link Fahrschueler}-Entitäten aus der Datenbank und
 * transformiert sie in {@link FahrschuelerListDTO}-Objekte, die für
 * Listenansichten optimiert sind.
 * </p>
 */
@Service
public class FahrschuelerListService implements IFahrschuelerListService {

	private final IFahrschuelerRepository fahrschuelerRepo;

	public FahrschuelerListService(IFahrschuelerRepository fahrschuelerRepo) {
		this.fahrschuelerRepo = fahrschuelerRepo;
	}

	/**
	 * Liefert eine Liste aller Fahrschüler als DTOs.
	 * <p>
	 * Die Methode ist transaktional, um Lazy-Loading-Probleme (z. B. bei
	 * Führerscheinen) zu vermeiden.
	 * </p>
	 *
	 * @return Liste von {@link FahrschuelerListDTO}; niemals {@code null}
	 */
	@Override
	@Transactional
	public List<FahrschuelerListDTO> getAllFahrschueler() {

		List<Fahrschueler> entities = fahrschuelerRepo.findAll();

		if (entities == null || entities.isEmpty()) {
			return Collections.emptyList();
		}

		return entities.stream()
				.filter(Objects::nonNull)
				.map(e -> {
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

					return new FahrschuelerListDTO(e.getFahrschuelerId(), e.getNachname(), e.getGeburtsdatum(),
							e.getTelefonnummer(), klassen, e.isBezahlt(), e.getMandant());
				})
				.toList();
	}
}
