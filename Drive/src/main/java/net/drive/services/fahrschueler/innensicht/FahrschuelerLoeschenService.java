package net.drive.services.fahrschueler.innensicht;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerLoeschenService;

/**
 * Service-Klasse zum Löschen eines Fahrschülers (Innensicht).
 * <p>
 * Diese Klasse kapselt die Geschäftslogik zum Entfernen eines
 * {@link Fahrschueler} aus dem System und führt notwendige Validierungen vor
 * dem Löschen durch.
 * </p>
 */
@Service
public class FahrschuelerLoeschenService implements IFahrschuelerLoeschenService {

	private final IFahrschuelerRepository fahrschuelerRepo;
	private final LogicResource logicResource;

	/**
	 * Konstruktor mit Dependency Injection.
	 *
	 * @param fahrschuelerRepo Repository für Fahrschüler
	 * @param logicResource    Resource für lokalisierte Fehlertexte
	 */
	public FahrschuelerLoeschenService(IFahrschuelerRepository fahrschuelerRepo, LogicResource logicResource) {
		this.fahrschuelerRepo = fahrschuelerRepo;
		this.logicResource = logicResource;
	}

	@Override
	public void deleteFahrschueler(UUID fahrschuelerId) {

		if (fahrschuelerId == null) {
			throw new IllegalArgumentException(logicResource.getMessage("KeinFahrschüler"));
		}

		Fahrschueler fahrschueler = fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("KeinFahrschüler")));

		fahrschuelerRepo.delete(fahrschueler);
	}

}
