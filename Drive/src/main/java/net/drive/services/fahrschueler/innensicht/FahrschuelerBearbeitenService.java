package net.drive.services.fahrschueler.innensicht;

import java.util.UUID;

import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerBearbeitenService;

/**
 * Service-Klasse zum Laden und Aktualisieren von Fahrschüler-Daten
 * (Innensicht).
 * <p>
 * Diese Klasse stellt die Geschäftslogik für das Bearbeiten eines
 * {@link Fahrschueler} bereit. Sie kapselt Zugriffe auf die Repositories und
 * führt Validierungen durch.
 * </p>
 */
@Service
public class FahrschuelerBearbeitenService implements IFahrschuelerBearbeitenService {

	/**
	 * Konstruktor-Injection der Abhängigkeiten.
	 *
	 * @param fahrschuelerRepo  Repository für Fahrschüler
	 * @param fuehrerscheinRepo Repository für Führerscheine
	 * @param logicResource     Message-/Logic-Resource für Fehlertexte
	 */
	private final IFahrschuelerRepository fahrschuelerRepo;
	private final IFuehrerscheinRepository fuehrerscheinRepo;
	private final LogicResource logicResource;

	/**
	 * Konstruktor-Injection der Abhängigkeiten.
	 *
	 * @param fahrschuelerRepo  Repository für Fahrschüler
	 * @param fuehrerscheinRepo Repository für Führerscheine
	 * @param logicResource     Message-/Logic-Resource für Fehlertexte
	 */
	public FahrschuelerBearbeitenService(IFahrschuelerRepository fahrschuelerRepo,
			IFuehrerscheinRepository fuehrerscheinRepo, LogicResource logicResource) {
		this.fahrschuelerRepo = fahrschuelerRepo;
		this.fuehrerscheinRepo = fuehrerscheinRepo;
		this.logicResource = logicResource;
	}

	@Override
	public Fahrschueler getFahrschueler(UUID fahrschuelerId) {
		if (fahrschuelerId == null) {
			throw new IllegalArgumentException(logicResource.getMessage("IDNull"));
		}
		return fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("FahrschuelerNichtGefunden")));
	}

	/**
	 * Aktualisiert einen bestehenden Fahrschüler.
	 * <p>
	 * Es werden Stammdaten (Name, Kontakt, etc.) sowie optional die zugeordneten
	 * Führerscheine aktualisiert. Bei Führerscheinen wird die bidirektionale
	 * Beziehung gepflegt (Zuordnung im Fahrschüler und Rückbezug im Führerschein).
	 * </p>
	 *
	 * @param udFahrschueler neue Werte (Update-Daten)
	 * @param fahrschuelerId ID des zu aktualisierenden Fahrschülers
	 * @return persistierter Fahrschüler nach Update
	 * @throws IllegalArgumentException wenn Input oder ID null ist, Pflichtfelder
	 *                                  fehlen oder der Datensatz nicht existiert
	 */
	@Override
	public Fahrschueler updateFahrschueler(Fahrschueler udFahrschueler, UUID fahrschuelerId) {

		if (udFahrschueler == null || fahrschuelerId == null) {
			throw new IllegalArgumentException(logicResource.getMessage("InputOderID"));
		}

		Fahrschueler updatedFahrschueler = fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId)
				.orElseThrow(() -> new IllegalArgumentException(logicResource.getMessage("FahrschuelerNichtGefunden")));

		if (updatedFahrschueler.getVorname() == null || updatedFahrschueler.getNachname() == null
				|| updatedFahrschueler.getVorname().isEmpty() || updatedFahrschueler.getNachname().isEmpty()) {
			throw new IllegalArgumentException(logicResource.getMessage("NameFehlen"));
		}
		// Übernahme der einfachen Felder aus dem Update-Objekt.
		updatedFahrschueler.setVorname(udFahrschueler.getVorname());
		updatedFahrschueler.setNachname(udFahrschueler.getNachname());
		updatedFahrschueler.setGeburtsdatum(udFahrschueler.getGeburtsdatum());
		updatedFahrschueler.setAdresse(udFahrschueler.getAdresse());
		updatedFahrschueler.setTelefonnummer(udFahrschueler.getTelefonnummer());
		updatedFahrschueler.setEmail(udFahrschueler.getEmail());
		updatedFahrschueler.setAnmeldedatum(udFahrschueler.getAnmeldedatum());
		updatedFahrschueler.setBezahlt(udFahrschueler.isBezahlt());
		updatedFahrschueler.setDokumente(udFahrschueler.getDokumente());
		updatedFahrschueler.setHinweis(udFahrschueler.getHinweis());
		updatedFahrschueler.setNotfallkontakt(udFahrschueler.getNotfallkontakt());

		if (udFahrschueler.getFuehrerscheine() != null && !udFahrschueler.getFuehrerscheine().isEmpty()) {
			// Clear existing relationships only when we have new Führerscheine
			if (!updatedFahrschueler.getFuehrerscheine().isEmpty()) {
				updatedFahrschueler.getFuehrerscheine().forEach(f -> f.getFahrschueler().remove(updatedFahrschueler));
				updatedFahrschueler.getFuehrerscheine().clear();
			}

			udFahrschueler.getFuehrerscheine().forEach(f -> {
				fuehrerscheinRepo.findByFuehrerscheinId(f.getFuehrerscheinId()).ifPresent(fuehrerschein -> {
					updatedFahrschueler.getFuehrerscheine().add(fuehrerschein);
					fuehrerschein.getFahrschueler().add(updatedFahrschueler);
				});
			});
		}
		return fahrschuelerRepo.save(updatedFahrschueler);
	}

}
