package net.drive;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import net.drive.config.LogicResource;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.innensicht.FahrschuelerLoeschenService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FahrschuelerLoeschenServiceTest {

    @Mock
    private IFahrschuelerRepository fahrschuelerRepo;

    @Mock
    private LogicResource logicResource;

    @InjectMocks
    private FahrschuelerLoeschenService service;

    private UUID fahrschuelerId;
    private Fahrschueler fahrschueler;

    @BeforeEach
    void setup() {
        fahrschuelerId = UUID.randomUUID();
        fahrschueler = new Fahrschueler();
        fahrschueler.setFahrschuelerId(fahrschuelerId);

       
        lenient().when(logicResource.getMessage("KeinFahrschüler")).thenReturn("KeinFahrschüler");
    }

    @Test
    void deleteFahrschueler_NullId_Throws() {
    	UUID nullId = null;
    	IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> 
            service.deleteFahrschueler(nullId)
        );
        assertEquals("KeinFahrschüler", ex.getMessage());
    }

    @Test
    void deleteFahrschueler_NotFound_Throws() {
        UUID id = UUID.randomUUID();
        when(fahrschuelerRepo.findByFahrschuelerId(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> 
            service.deleteFahrschueler(id)
        );
        assertEquals("KeinFahrschüler", ex.getMessage());
    }

    @Test
    void deleteFahrschueler_HappyPath() {
        when(fahrschuelerRepo.findByFahrschuelerId(fahrschuelerId)).thenReturn(Optional.of(fahrschueler));

        service.deleteFahrschueler(fahrschuelerId);

        verify(fahrschuelerRepo).delete(fahrschueler);
    }
}
