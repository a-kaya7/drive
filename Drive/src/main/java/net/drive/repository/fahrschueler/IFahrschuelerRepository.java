package net.drive.repository.fahrschueler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.model.entities.fahrschueler.Pruefungsstatus; // <-- Gerekirse package'ı düzelt

@Repository
public interface IFahrschuelerRepository extends JpaRepository<Fahrschueler, UUID> {

    boolean existsByNachnameAndGeburtsdatum(String nachname, LocalDate geburtsdatum);
    Optional<Fahrschueler> findFarhschuelerByVorname(String vorname);
    Optional<Fahrschueler> findByFahrschuelerId(UUID fahrschuelerId);

    // Senaryo 2: sadece Klasse B
    @Query("""
        SELECT DISTINCT f
        FROM Fahrschueler f
        JOIN f.fuehrerscheine fs
        WHERE fs.fuehrerscheinKlasse = :klasse
    """)
    List<Fahrschueler> findByFuehrerscheinKlasse(@Param("klasse") String klasse);

    // Senaryo 3: Klasse B + 30 yaş altı
    @Query("""
        SELECT DISTINCT f
        FROM Fahrschueler f
        JOIN f.fuehrerscheine fs
        WHERE fs.fuehrerscheinKlasse = :klasse
          AND f.geburtsdatum > :bornAfter
    """)
    List<Fahrschueler> findByKlasseAndBornAfter(
            @Param("klasse") String klasse,
            @Param("bornAfter") LocalDate bornAfter
    );

    // Senaryo 4: Klasse B + bezahlt=true + status=NOCH_OFFEN
    @Query("""
        SELECT DISTINCT f
        FROM Fahrschueler f
        JOIN f.fuehrerscheine fs
        WHERE fs.fuehrerscheinKlasse = :klasse
          AND f.bezahlt = :bezahlt
          AND f.pruefungsstatus = :status
    """)
    List<Fahrschueler> findByKlasseAndBezahltAndStatus(
            @Param("klasse") String klasse,
            @Param("bezahlt") boolean bezahlt,
            @Param("status") Pruefungsstatus status
    );
}
