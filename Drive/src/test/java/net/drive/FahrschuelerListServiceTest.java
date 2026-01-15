package net.drive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.innensicht.FahrschuelerListService;

@ExtendWith(MockitoExtension.class)
public class FahrschuelerListServiceTest {

    @Mock
    private IFahrschuelerRepository fahrschuelerRepo;

    @InjectMocks
    private FahrschuelerListService service;

    private Fahrschueler fahrschueler;

    @BeforeEach
    void setup() {
        fahrschueler = new Fahrschueler();
        fahrschueler.setFahrschuelerId(UUID.randomUUID());
        fahrschueler.setNachname("Mustermann");
        fahrschueler.setGeburtsdatum(LocalDate.of(2000, 1, 1));
        fahrschueler.setTelefonnummer("0123456789");
        fahrschueler.setBezahlt(true);
        fahrschueler.setMandant("Mandant1");

        Fuehrerschein fs = new Fuehrerschein();
        fs.setFuehrerscheinKlasse("B");
        fs.setFahrschueler(new HashSet<>());

        fahrschueler.setFuehrerscheine(Set.of(fs));
    }

    // ✅ Branch: entities == null
    @Test
    void getAllFahrschueler_NullList_ReturnsEmpty() {
        when(fahrschuelerRepo.findAll()).thenReturn(null);

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
void getAllFahrschueler_EmptyList_ReturnsEmpty() {
    when(fahrschuelerRepo.findAll()).thenReturn(List.of()); // boş immutable liste

    List<FahrschuelerListDTO> result = service.getAllFahrschueler();

    assertNotNull(result);
    assertTrue(result.isEmpty());
}


  @Test
void getAllFahrschueler_ListContainsNullEntity_IgnoresNull() {
    when(fahrschuelerRepo.findAll()).thenReturn(java.util.Arrays.asList(null, fahrschueler));

    List<FahrschuelerListDTO> result = service.getAllFahrschueler();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Mustermann", result.get(0).nachname());
}


    // ✅ Branch: e.getFuehrerscheine() == null
    @Test
    void getAllFahrschueler_FuehrerscheineNull_MapsToEmptySet() {
        Fahrschueler s = new Fahrschueler();
        s.setFahrschuelerId(UUID.randomUUID());
        s.setNachname("NullFS");
        s.setGeburtsdatum(LocalDate.of(1999, 1, 1));
        s.setTelefonnummer("000");
        s.setBezahlt(false);
        s.setMandant("M1");
        s.setFuehrerscheine(null);

        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s));

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertEquals(1, result.size());
        assertTrue(result.get(0).fuehrerscheine().isEmpty());
    }

    // ✅ Branch: e.getFuehrerscheine() empty
    @Test
    void getAllFahrschueler_FuehrerscheineEmpty_MapsToEmptySet() {
        Fahrschueler s = new Fahrschueler();
        s.setFahrschuelerId(UUID.randomUUID());
        s.setNachname("EmptyFS");
        s.setGeburtsdatum(LocalDate.of(1998, 1, 1));
        s.setTelefonnummer("111");
        s.setBezahlt(true);
        s.setMandant("M2");
        s.setFuehrerscheine(Set.of()); // empty

        when(fahrschuelerRepo.findAll()).thenReturn(List.of(s));

        List<FahrschuelerListDTO> result = service.getAllFahrschueler();

        assertEquals(1, result.size());
        assertTrue(result.get(0).fuehrerscheine().isEmpty());
    }

    @Test
void getAllFahrschueler_FuehrerscheinOrKlasseNull_AreFilteredOut() {
    Fahrschueler s = new Fahrschueler();
    s.setFahrschuelerId(UUID.randomUUID());
    s.setNachname("FilterTest");
    s.setGeburtsdatum(LocalDate.of(2001, 2, 2));
    s.setTelefonnummer("222");
    s.setBezahlt(true);
    s.setMandant("M3");

    Fuehrerschein fsNullClass = new Fuehrerschein();
    fsNullClass.setFuehrerscheinKlasse(null);
    fsNullClass.setFahrschueler(new HashSet<>());

    HashSet<Fuehrerschein> set = new HashSet<>();
    set.add(null);         // ✅ null eleman ekledik
    set.add(fsNullClass);  // ✅ klasse null
    s.setFuehrerscheine(set);

    when(fahrschuelerRepo.findAll()).thenReturn(List.of(s));

    List<FahrschuelerListDTO> result = service.getAllFahrschueler();

    assertEquals(1, result.size());
    assertTrue(result.get(0).fuehrerscheine().isEmpty());
}

}
