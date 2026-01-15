package net.drive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.drive.config.LogicResource;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.innensicht.FahrschuelerBearbeitenService;

@ExtendWith(MockitoExtension.class)
public class FahrschuelerBearbeitenServiceTest {

    @Mock
    private IFahrschuelerRepository fahrschuelerRepo;

    @Mock
    private IFuehrerscheinRepository fuehrerscheinRepo;

    @Mock
    private LogicResource logicResource;

    @InjectMocks
    private FahrschuelerBearbeitenService service;

    private UUID fahrschuelerId;
    private Fahrschueler existing;
    private Fahrschueler update;

    @BeforeEach
    void setup() {
        fahrschuelerId = UUID.randomUUID();

        existing = new Fahrschueler();
        existing.setVorname("Max");
        existing.setNachname("Mustermann");
        existing.setGeburtsdatum(LocalDate.of(2000, 1, 1));
        existing.setFuehrerscheine(new HashSet<>());

        update = new Fahrschueler();
        update.setVorname("Maximilian");
        update.setNachname("Mustermann");
        update.setGeburtsdatum(LocalDate.of(2000, 1, 1));
        update.setFuehrerscheine(new HashSet<>());
    }

    // ---------------- GET ----------------

    @Test
    void getFahrschueler_IdNull_Throws() {
        when(logicResource.getMessage("IDNull")).thenReturn("IDNull");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.getFahrschueler(null)
        );

        assertEquals("IDNull", ex.getMessage());
    }

    @Test
    void getFahrschueler_NotFound_Throws() {
        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getFahrschueler(fahrschuelerId)
        );
    }

    @Test
    void getFahrschueler_HappyPath() {
        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));

        Fahrschueler result = service.getFahrschueler(fahrschuelerId);

        assertNotNull(result);
        assertEquals("Max", result.getVorname());
    }

    // ---------------- UPDATE ----------------

    @Test
    void updateFahrschueler_InputOderIdNull_Throws_BothNull() {
        when(logicResource.getMessage("InputOderID")).thenReturn("InputOderID");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(null, null)
        );

        assertEquals("InputOderID", ex.getMessage());
    }

    
    @Test
    void updateFahrschueler_InputOderIdNull_Throws_InputNullOnly() {
        when(logicResource.getMessage("InputOderID")).thenReturn("InputOderID");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(null, fahrschuelerId)
        );

        assertEquals("InputOderID", ex.getMessage());
    }

  
    @Test
    void updateFahrschueler_InputOderIdNull_Throws_IdNullOnly() {
        when(logicResource.getMessage("InputOderID")).thenReturn("InputOderID");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(update, null)
        );

        assertEquals("InputOderID", ex.getMessage());
    }

    @Test
    void updateFahrschueler_NotFound_Throws() {
        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(update, fahrschuelerId)
        );
    }

    @Test
    void updateFahrschueler_NameFehlen_Throws_EmptyStrings() {
        when(logicResource.getMessage("NameFehlen")).thenReturn("NameFehlen");

        existing.setVorname("");
        existing.setNachname("");

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(update, fahrschuelerId)
        );

        assertEquals("NameFehlen", ex.getMessage());
    }

    
    @Test
    void updateFahrschueler_NameFehlen_Throws_VornameNull() {
        when(logicResource.getMessage("NameFehlen")).thenReturn("NameFehlen");

        existing.setVorname(null);
        existing.setNachname("Mustermann");

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(update, fahrschuelerId)
        );

        assertEquals("NameFehlen", ex.getMessage());
    }

   
    @Test
    void updateFahrschueler_NameFehlen_Throws_NachnameNull() {
        when(logicResource.getMessage("NameFehlen")).thenReturn("NameFehlen");

        existing.setVorname("Max");
        existing.setNachname(null);

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFahrschueler(update, fahrschuelerId)
        );

        assertEquals("NameFehlen", ex.getMessage());
    }

    @Test
    void updateFahrschueler_FuehrerscheineNull_Allows() {
        update.setFuehrerscheine(null);

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertNotNull(result);
        verifyNoInteractions(fuehrerscheinRepo);
    }

    
    @Test
    void updateFahrschueler_FuehrerscheineEmpty_Allows_NoRepoCalls() {
        update.setFuehrerscheine(Set.of()); 

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertNotNull(result);
        verifyNoInteractions(fuehrerscheinRepo);
    }

    @Test
    void updateFahrschueler_WithoutFuehrerschein_HappyPath() {
       
        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertEquals("Maximilian", result.getVorname());
        verify(fahrschuelerRepo).save(existing);
        verifyNoInteractions(fuehrerscheinRepo);
    }

    @Test
    void updateFahrschueler_WithFuehrerschein_HappyPath() {
        Fuehrerschein fs = new Fuehrerschein();
        fs.setFuehrerscheinId(UUID.randomUUID());
        fs.setFahrschueler(new HashSet<>());

        update.setFuehrerscheine(Set.of(fs));

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fuehrerscheinRepo.findByFuehrerscheinId(any()))
                .thenReturn(Optional.of(fs));
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertEquals(1, result.getFuehrerscheine().size());
        verify(fahrschuelerRepo).save(existing);
        verify(fuehrerscheinRepo, atLeastOnce()).findByFuehrerscheinId(any());
    }

    
    @Test
    void updateFahrschueler_WithFuehrerschein_NotFound_Ignored() {
        Fuehrerschein fs = new Fuehrerschein();
        fs.setFuehrerscheinId(UUID.randomUUID());
        fs.setFahrschueler(new HashSet<>());

        update.setFuehrerscheine(Set.of(fs));

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fuehrerscheinRepo.findByFuehrerscheinId(any()))
                .thenReturn(Optional.empty());
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertNotNull(result);
        assertTrue(result.getFuehrerscheine().isEmpty());
        verify(fuehrerscheinRepo, atLeastOnce()).findByFuehrerscheinId(any());
    }

    
    @Test
    void updateFahrschueler_ReplacesExistingFuehrerscheine_RemovesOldAndAddsNew() {
       
        Fuehrerschein oldFs = new Fuehrerschein();
        oldFs.setFuehrerscheinId(UUID.randomUUID());
        oldFs.setFahrschueler(new HashSet<>());
        oldFs.getFahrschueler().add(existing);
        existing.getFuehrerscheine().add(oldFs);

        
        Fuehrerschein newFsRequest = new Fuehrerschein();
        newFsRequest.setFuehrerscheinId(UUID.randomUUID());
        newFsRequest.setFahrschueler(new HashSet<>());
        update.setFuehrerscheine(Set.of(newFsRequest));

        Fuehrerschein newFsFromRepo = new Fuehrerschein();
        newFsFromRepo.setFuehrerscheinId(newFsRequest.getFuehrerscheinId());
        newFsFromRepo.setFahrschueler(new HashSet<>());

        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId))
                .thenReturn(Optional.of(existing));
        when(fuehrerscheinRepo.findByFuehrerscheinId(newFsRequest.getFuehrerscheinId()))
                .thenReturn(Optional.of(newFsFromRepo));
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Fahrschueler result = service.updateFahrschueler(update, fahrschuelerId);

        assertNotNull(result);
       
        assertEquals(1, result.getFuehrerscheine().size());
        assertTrue(result.getFuehrerscheine().contains(newFsFromRepo));
        assertFalse(result.getFuehrerscheine().contains(oldFs));

        
        assertFalse(oldFs.getFahrschueler().contains(existing));
        
        assertTrue(newFsFromRepo.getFahrschueler().contains(existing));
    }
}
