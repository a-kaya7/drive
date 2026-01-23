package net.drive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.model.entities.fahrschueler.Pruefungsstatus;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.innensicht.FahrschuelerListService;

@ExtendWith(MockitoExtension.class)
class FahrschuelerListServiceTest {

    @Mock
    private IFahrschuelerRepository fahrschuelerRepo;

    @InjectMocks
    private FahrschuelerListService service;

    private Fahrschueler s1;
    private Fahrschueler s2;

    @BeforeEach
    void setup() {
        s1 = makeStudent("Max", "Mustermann", true, Pruefungsstatus.THEORIE_BESTANDEN, "Mandant1",
                Set.of(makeFs("B")));
        s2 = makeStudent("Anna", "Müller", false, Pruefungsstatus.NOCH_OFFEN, "Mandant2",
                Set.of(makeFs("A"), makeFs("BE")));
    }

    // ---------------------------------------------------------------------
    // getAllFahrschueler(): C0/C1/C2
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("C0/C2: findAll() null -> emptyList")
    void getAllFahrschueler_repoNull_returnsEmpty() {
        when(fahrschuelerRepo.findAll()).thenReturn(null);

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fahrschuelerRepo).findAll();
    }

    @Test
    @DisplayName("C0/C2: findAll() empty -> emptyList")
    void getAllFahrschueler_repoEmpty_returnsEmpty() {
        when(fahrschuelerRepo.findAll()).thenReturn(List.of());

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fahrschuelerRepo).findAll();
    }

    @Test
    @DisplayName("C1: null-Entity in Liste wird gefiltert")
    void getAllFahrschueler_listContainsNullEntity_isIgnored() {
        when(fahrschuelerRepo.findAll()).thenReturn(Arrays.asList(null, s1));

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertEquals(1, result.size());
        assertEquals("Max", result.get(0).vorname());
        assertEquals("Mustermann", result.get(0).nachname());
    }

    @Test
    @DisplayName("C1/C2: fuehrerscheine null/empty -> klassen emptySet")
    void getAllFahrschueler_fuehrerscheineNullOrEmpty_mapsEmptySet() {
        Fahrschueler a = makeStudent("A", "NullFS", true, Pruefungsstatus.NOCH_OFFEN, "M1", null);
        Fahrschueler b = makeStudent("B", "EmptyFS", true, Pruefungsstatus.NOCH_OFFEN, "M1", Set.of());

        when(fahrschuelerRepo.findAll()).thenReturn(List.of(a, b));

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertEquals(2, result.size());
        assertEquals(Set.of(), result.get(0).fuehrerscheine());
        assertEquals(Set.of(), result.get(1).fuehrerscheine());
    }

    @Test
    @DisplayName("C1/C2: null Fuehrerschein + null Klasse werden gefiltert")
    void getAllFahrschueler_fuehrerscheinNull_and_klasseNull_areFiltered() {
        Fuehrerschein fsNullClass = makeFs(null);

        HashSet<Fuehrerschein> set = new HashSet<>();
        set.add(null);
        set.add(fsNullClass);

        Fahrschueler x = makeStudent("X", "Filter", true, Pruefungsstatus.NOCH_OFFEN, "M2", set);

        when(fahrschuelerRepo.findAll()).thenReturn(List.of(x));

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertEquals(1, result.size());
        assertEquals(Set.of(), result.get(0).fuehrerscheine());
    }

    // ---------------------------------------------------------------------
    // getFahrschuelerBenchmark(): C1 (Szenarien) + C2/C3 (MC/DC)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("C1: Szenario 1 (keine Filter) -> getAllFahrschueler()")
    void benchmark_szenario1_noFilter_delegatesToGetAll() {
        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s1, s2));

        List<FahrschuelerListDTO> result = service.getFahrschuelerBenchmark(null, null, null, null);

        assertEquals(2, result.size());
        verify(fahrschuelerRepo).findAll();
        verifyNoMoreInteractions(fahrschuelerRepo);
    }

    @Test
    @DisplayName("C1: Szenario 2 (nur Klasse) -> findByFuehrerscheinKlasse")
    void benchmark_szenario2_onlyKlasse_callsFindByFuehrerscheinKlasse() {
        when(fahrschuelerRepo.findByFuehrerscheinKlasse("B")).thenReturn(Arrays.asList(null, s1));

        List<FahrschuelerListDTO> result = service.getFahrschuelerBenchmark("  B  ", null, null, "   ");

        assertEquals(1, result.size());
        assertEquals(Set.of("B"), result.get(0).fuehrerscheine());

        verify(fahrschuelerRepo).findByFuehrerscheinKlasse("B");
        verify(fahrschuelerRepo, never()).findAll();
        verify(fahrschuelerRepo, never()).findByKlasseAndBornAfter(anyString(), any(LocalDate.class));
        verify(fahrschuelerRepo, never()).findByKlasseAndBezahltAndStatus(anyString(), anyBoolean(), any());
    }

    @Test
    @DisplayName("C1: Szenario 3 (Klasse + ageMax) -> findByKlasseAndBornAfter mit korrektem bornAfter")
    void benchmark_szenario3_klasseAndAge_callsFindByKlasseAndBornAfter() {
        int ageMax = 29;
        when(fahrschuelerRepo.findByKlasseAndBornAfter(eq("B"), any(LocalDate.class))).thenReturn(List.of(s1));

        List<FahrschuelerListDTO> result = service.getFahrschuelerBenchmark("B", ageMax, null, null);

        assertEquals(1, result.size());

        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);
        verify(fahrschuelerRepo).findByKlasseAndBornAfter(eq("B"), captor.capture());

        LocalDate expectedBornAfter = LocalDate.now().minusYears((long) ageMax + 1L);
        assertEquals(expectedBornAfter, captor.getValue());

        verify(fahrschuelerRepo, never()).findAll();
        verify(fahrschuelerRepo, never()).findByFuehrerscheinKlasse(anyString());
        verify(fahrschuelerRepo, never()).findByKlasseAndBezahltAndStatus(anyString(), anyBoolean(), any());
    }

    @Test
    @DisplayName("C1: Szenario 4 (Klasse + bezahlt + status) -> findByKlasseAndBezahltAndStatus")
    void benchmark_szenario4_klasseBezahltStatus_callsFindByKlasseAndBezahltAndStatus() {
        when(fahrschuelerRepo.findByKlasseAndBezahltAndStatus("B", true, Pruefungsstatus.THEORIE_BESTANDEN))
                .thenReturn(Arrays.asList(null, s1));

        List<FahrschuelerListDTO> result =
                service.getFahrschuelerBenchmark("  B ", null, Boolean.TRUE, "  THEORIE_BESTANDEN ");

        assertEquals(1, result.size());
        assertEquals("Max", result.get(0).vorname());

        verify(fahrschuelerRepo).findByKlasseAndBezahltAndStatus("B", true, Pruefungsstatus.THEORIE_BESTANDEN);
        verify(fahrschuelerRepo, never()).findAll();
        verify(fahrschuelerRepo, never()).findByFuehrerscheinKlasse(anyString());
        verify(fahrschuelerRepo, never()).findByKlasseAndBornAfter(anyString(), any(LocalDate.class));
    }

    @Test
    @DisplayName("C1: Fallback (Parameterkombination passt zu keinem Szenario) -> getAllFahrschueler()")
    void benchmark_fallback_delegatesToGetAll() {
        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s1));

        List<FahrschuelerListDTO> result = service.getFahrschuelerBenchmark("B", 30, true, null);

        assertEquals(1, result.size());
        verify(fahrschuelerRepo).findAll();
        verify(fahrschuelerRepo, never()).findByFuehrerscheinKlasse(anyString());
        verify(fahrschuelerRepo, never()).findByKlasseAndBornAfter(anyString(), any(LocalDate.class));
        verify(fahrschuelerRepo, never()).findByKlasseAndBezahltAndStatus(anyString(), anyBoolean(), any());
    }

    // ---------------------------------------------------------------------
    // C3 (MC/DC) – gezielte Toggle-Tests, damit atomare Bedingungen Entscheidung beeinflussen
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("C3(MC/DC): D1 wird false durch status!=blank (nur diese Bedingung toggeln) -> Fallback")
    void mcdc_toggleStatus_breaksNoFilterDecision() {
        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s1));

        List<FahrschuelerListDTO> result = service.getFahrschuelerBenchmark(null, null, null, "X");

        assertEquals(1, result.size());
        verify(fahrschuelerRepo).findAll();
    }

    @Test
    @DisplayName("C3(MC/DC): klasse blank vs nicht blank toggelt zwischen Szenario 1 und 2")
    void mcdc_toggleKlasse_betweenScenario1and2() {
        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s1));
        when(fahrschuelerRepo.findByFuehrerscheinKlasse("B")).thenReturn(List.of(s1));

        List<FahrschuelerListDTO> r1 = service.getFahrschuelerBenchmark("   ", null, null, "   ");
        List<FahrschuelerListDTO> r2 = service.getFahrschuelerBenchmark("B", null, null, "   ");

        assertEquals(1, r1.size());
        assertEquals(1, r2.size());

        verify(fahrschuelerRepo).findAll();
        verify(fahrschuelerRepo).findByFuehrerscheinKlasse("B");
    }

    @Test
    @DisplayName("C3(MC/DC): ageMax null vs nicht-null toggelt zwischen Szenario 2 und 3")
    void mcdc_toggleAgeMax_betweenScenario2and3() {
        when(fahrschuelerRepo.findByFuehrerscheinKlasse("B")).thenReturn(List.of(s1));
        when(fahrschuelerRepo.findByKlasseAndBornAfter(eq("B"), any(LocalDate.class))).thenReturn(List.of(s1));

        List<FahrschuelerListDTO> r1 = service.getFahrschuelerBenchmark("B", null, null, null);
        List<FahrschuelerListDTO> r2 = service.getFahrschuelerBenchmark("B", 10, null, null);

        assertEquals(1, r1.size());
        assertEquals(1, r2.size());

        verify(fahrschuelerRepo).findByFuehrerscheinKlasse("B");
        verify(fahrschuelerRepo).findByKlasseAndBornAfter(eq("B"), any(LocalDate.class));
    }

    @Test
    @DisplayName("C3(MC/DC): bezahlt null vs nicht-null (bei status!=blank) toggelt Richtung Szenario 4")
    void mcdc_toggleBezahlt_toScenario4() {
        when(fahrschuelerRepo.findByFuehrerscheinKlasse("B")).thenReturn(List.of(s1));
        when(fahrschuelerRepo.findByKlasseAndBezahltAndStatus("B", true, Pruefungsstatus.THEORIE_BESTANDEN))
                .thenReturn(List.of(s1));

        List<FahrschuelerListDTO> r1 = service.getFahrschuelerBenchmark("B", null, null, "   ");
        List<FahrschuelerListDTO> r2 = service.getFahrschuelerBenchmark("B", null, true, "THEORIE_BESTANDEN");

        assertEquals(1, r1.size());
        assertEquals(1, r2.size());

        verify(fahrschuelerRepo).findByFuehrerscheinKlasse("B");
        verify(fahrschuelerRepo).findByKlasseAndBezahltAndStatus("B", true, Pruefungsstatus.THEORIE_BESTANDEN);
    }

    // ---------------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------------

    private static Fahrschueler makeStudent(
            String vorname,
            String nachname,
            boolean bezahlt,
            Pruefungsstatus pruefungsstatus,
            String mandant,
            Set<Fuehrerschein> fuehrerscheine
    ) {
        Fahrschueler s = new Fahrschueler();
        s.setFahrschuelerId(UUID.randomUUID());
        s.setVorname(vorname);
        s.setNachname(nachname);
        s.setGeburtsdatum(LocalDate.of(2000, 1, 1));
        s.setAdresse(null);
        s.setTelefonnummer("000");
        s.setBezahlt(bezahlt);
        s.setPruefungsstatus(pruefungsstatus);
        s.setMandant(mandant);
        s.setFuehrerscheine(fuehrerscheine);
        return s;
    }

    private static Fuehrerschein makeFs(String klasse) {
        Fuehrerschein fs = new Fuehrerschein();
        fs.setFuehrerscheinKlasse(klasse);
        fs.setFahrschueler(new HashSet<>());
        return fs;
    }
}