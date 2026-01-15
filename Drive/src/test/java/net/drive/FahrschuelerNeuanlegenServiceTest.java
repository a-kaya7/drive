package net.drive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import jakarta.servlet.http.HttpServletRequest;
import net.drive.config.JwtService;
import net.drive.config.LogicResource;
import net.drive.model.datentypen.Adresse;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;
import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.model.entities.administration.allgemein.organisation.Fuehrerschein;
import net.drive.repository.administration.allgemein.organisation.IFuehrerscheinRepository;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.fahrschueler.innensicht.FahrschuelerNeuanlegenService;

@ExtendWith(MockitoExtension.class)
public class FahrschuelerNeuanlegenServiceTest {

    @Mock
    private IFahrschuelerRepository fahrschuelerRepo;

    @Mock
    private IFuehrerscheinRepository fuehrerscheinRepo;

    @Mock
    private JwtService jwtService;

    @Mock
    private LogicResource logicResource;

    @InjectMocks
    private FahrschuelerNeuanlegenService service;

    private FahrschuelerDTO validDto;
    private UUID fuehrerscheinId;

    @BeforeEach
    void setup() {
        lenient().when(logicResource.getMessage("VorUndNachnameDatum")).thenReturn("VorUndNachnameDatum");
        lenient().when(logicResource.getMessage("FahrschuelerVorhanden")).thenReturn("FahrschuelerVorhanden");

        Adresse testAdresse = new Adresse();
        testAdresse.setStrasse("Musterstraße 1");
        testAdresse.setPlz("12345");
        testAdresse.setOrt("Berlin");
        testAdresse.setLand("Deutschland");

        // Fuehrerschein set (DTO)
        Set<FuehrerscheinDTO> fuehrerscheinSet = new HashSet<>();
        fuehrerscheinId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        FuehrerscheinDTO fuehrerscheinDTO = mock(FuehrerscheinDTO.class);
        lenient().when(fuehrerscheinDTO.fuehrerscheinId()).thenReturn(fuehrerscheinId);
        fuehrerscheinSet.add(fuehrerscheinDTO);

        validDto = new FahrschuelerDTO(
                UUID.randomUUID(),              // fahrschuelerId
                "Max",                          // vorname
                "Mustermann",                   // nachname
                LocalDate.of(2000, 1, 1),       // geburtsdatum
                testAdresse,                    // adresse
                "0123456789",                   // telefonnummer
                "max@test.com",                 // email
                fuehrerscheinSet,               // fuehrerscheine
                LocalDate.now(),                // anmeldedatum
                null,                           // pruefungsstatus
                false,                          // bezahlt
                null,                           // dokumente
                null,                           // hinweis
                null,                           // notfallkontakt
                null,                           // ersteller
                null                            // mandant
        );

        // default: fuehrerschein found (NPE riskini engelle)
        Fuehrerschein fs = new Fuehrerschein();
        fs.setFuehrerscheinId(fuehrerscheinId);
        fs.setFahrschueler(new HashSet<>());

        lenient().when(fuehrerscheinRepo.findByFuehrerscheinId(fuehrerscheinId))
                .thenReturn(Optional.of(fs));
    }

    @Test
    void createFahrschueler_HappyPath() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        HttpServletRequest request = mock(HttpServletRequest.class);
        FahrschuelerDTO result = service.createFahrschueler(validDto, request);

        assertNotNull(result);
        assertEquals("Max", result.vorname());
        assertEquals("Mustermann", result.nachname());
        assertEquals("max@test.com", result.email());
        verify(fahrschuelerRepo).save(any());
    }

    // ✅ Bearer token branch
    @Test
    void createFahrschueler_WithBearerToken_SetsErstellerAndMandant() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");

        when(jwtService.getBenutzerkennungFromToken("token123")).thenReturn("ahmet");
        when(jwtService.getMandantFromToken("token123")).thenReturn("m1");

        FahrschuelerDTO result = service.createFahrschueler(validDto, request);

        assertNotNull(result);
        assertEquals("ahmet", result.ersteller());
        assertEquals("m1", result.mandant());

        verify(jwtService).getBenutzerkennungFromToken("token123");
        verify(jwtService).getMandantFromToken("token123");
    }

    // ✅ authHeader null branch
    @Test
    void createFahrschueler_AuthHeaderNull_DoesNotCallJwtService() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        FahrschuelerDTO result = service.createFahrschueler(validDto, request);

        assertNotNull(result);
        assertEquals("", result.ersteller());
        assertEquals("", result.mandant());

        verifyNoInteractions(jwtService);
    }

    // ✅ Non-bearer auth header branch (jwtService çağrılmamalı)
    @Test
    void createFahrschueler_WithNonBearerAuthHeader_DoesNotCallJwtService() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Basic abcdef");

        FahrschuelerDTO result = service.createFahrschueler(validDto, request);

        assertNotNull(result);
        assertEquals("", result.ersteller());
        assertEquals("", result.mandant());

        verifyNoInteractions(jwtService);
    }

    // Null Vorname
    @Test
    void createFahrschueler_NullVorname_Throws() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(),
                null, // vorname null
                validDto.nachname(),
                validDto.geburtsdatum(),
                validDto.adresse(),
                validDto.telefonnummer(),
                validDto.email(),
                validDto.fuehrerscheine(),
                validDto.anmeldedatum(),
                validDto.pruefungsstatus(),
                validDto.bezahlt(),
                validDto.dokumente(),
                validDto.hinweis(),
                validDto.notfallkontakt(),
                validDto.ersteller(),
                validDto.mandant()
        );

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.createFahrschueler(dto, mock(HttpServletRequest.class))
        );

        assertTrue(ex.getMessage().contains("VorUndNachnameDatum"));
    }

    // Null Nachname
    @Test
    void createFahrschueler_NullNachname_Throws() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(),
                validDto.vorname(),
                null, // nachname null
                validDto.geburtsdatum(),
                validDto.adresse(),
                validDto.telefonnummer(),
                validDto.email(),
                validDto.fuehrerscheine(),
                validDto.anmeldedatum(),
                validDto.pruefungsstatus(),
                validDto.bezahlt(),
                validDto.dokumente(),
                validDto.hinweis(),
                validDto.notfallkontakt(),
                validDto.ersteller(),
                validDto.mandant()
        );

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.createFahrschueler(dto, mock(HttpServletRequest.class))
        );

        assertTrue(ex.getMessage().contains("VorUndNachnameDatum"));
    }

    // Null Geburtsdatum
    @Test
    void createFahrschueler_NullGeburtsdatum_Throws() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(),
                validDto.vorname(),
                validDto.nachname(),
                null, // geburtsdatum null
                validDto.adresse(),
                validDto.telefonnummer(),
                validDto.email(),
                validDto.fuehrerscheine(),
                validDto.anmeldedatum(),
                validDto.pruefungsstatus(),
                validDto.bezahlt(),
                validDto.dokumente(),
                validDto.hinweis(),
                validDto.notfallkontakt(),
                validDto.ersteller(),
                validDto.mandant()
        );

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.createFahrschueler(dto, mock(HttpServletRequest.class))
        );

        assertTrue(ex.getMessage().contains("VorUndNachnameDatum"));
    }

    // Empty Vorname (boş string kabul ediliyorsa)
    @Test
    void createFahrschueler_EmptyVorname_Allows() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(),
                "", // empty
                validDto.nachname(),
                validDto.geburtsdatum(),
                validDto.adresse(),
                validDto.telefonnummer(),
                validDto.email(),
                validDto.fuehrerscheine(),
                validDto.anmeldedatum(),
                validDto.pruefungsstatus(),
                validDto.bezahlt(),
                validDto.dokumente(),
                validDto.hinweis(),
                validDto.notfallkontakt(),
                validDto.ersteller(),
                validDto.mandant()
        );

        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        FahrschuelerDTO result = service.createFahrschueler(dto, mock(HttpServletRequest.class));

        assertNotNull(result);
        assertEquals("", result.vorname());
        verify(fahrschuelerRepo, never()).existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class));
    }

    // Null Email
    @Test
    void createFahrschueler_NullEmail_Allows() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(), validDto.vorname(), validDto.nachname(), validDto.geburtsdatum(),
                validDto.adresse(), validDto.telefonnummer(), null,
                validDto.fuehrerscheine(), validDto.anmeldedatum(), validDto.pruefungsstatus(),
                validDto.bezahlt(), validDto.dokumente(), validDto.hinweis(), validDto.notfallkontakt(),
                validDto.ersteller(), validDto.mandant()
        );

        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any()))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        FahrschuelerDTO result = service.createFahrschueler(dto, mock(HttpServletRequest.class));
        assertNotNull(result);
        assertNull(result.email());
    }

    // Empty Fuehrerschein
    @Test
    void createFahrschueler_EmptyFuehrerschein_Allows() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(), validDto.vorname(), validDto.nachname(), validDto.geburtsdatum(),
                validDto.adresse(), validDto.telefonnummer(), validDto.email(),
                Set.of(), validDto.anmeldedatum(), validDto.pruefungsstatus(),
                validDto.bezahlt(), validDto.dokumente(), validDto.hinweis(), validDto.notfallkontakt(),
                validDto.ersteller(), validDto.mandant()
        );

        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any()))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        FahrschuelerDTO result = service.createFahrschueler(dto, mock(HttpServletRequest.class));
        assertNotNull(result);
        assertTrue(result.fuehrerscheine().isEmpty());
    }

    // ✅ Fuehrerscheine null branch
    @Test
    void createFahrschueler_FuehrerscheineNull_AllowsAndNoFuehrerscheinRepoCalls() {
        FahrschuelerDTO dto = new FahrschuelerDTO(
                validDto.fahrschuelerId(),
                validDto.vorname(),
                validDto.nachname(),
                validDto.geburtsdatum(),
                validDto.adresse(),
                validDto.telefonnummer(),
                validDto.email(),
                null, // 👈 fuehrerscheine null
                validDto.anmeldedatum(),
                validDto.pruefungsstatus(),
                validDto.bezahlt(),
                validDto.dokumente(),
                validDto.hinweis(),
                validDto.notfallkontakt(),
                validDto.ersteller(),
                validDto.mandant()
        );

        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        FahrschuelerDTO result = service.createFahrschueler(dto, mock(HttpServletRequest.class));

        assertNotNull(result);
        assertTrue(result.fuehrerscheine().isEmpty());

        verifyNoInteractions(fuehrerscheinRepo);
    }

    // ✅ Fuehrerschein present: result set dolmalı
    @Test
    void createFahrschueler_FuehrerscheinPresent_AddsFuehrerscheinToResult() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        FahrschuelerDTO result = service.createFahrschueler(validDto, mock(HttpServletRequest.class));

        assertNotNull(result);
        assertEquals(1, result.fuehrerscheine().size());
        assertTrue(result.fuehrerscheine().stream().anyMatch(f -> fuehrerscheinId.equals(f.fuehrerscheinId())));

        verify(fuehrerscheinRepo, atLeastOnce()).findByFuehrerscheinId(fuehrerscheinId);
    }

    // ✅ Fuehrerschein not found branch (Optional.empty)
    @Test
    void createFahrschueler_FuehrerscheinNotFound_IsIgnored() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(false);
        when(fahrschuelerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        when(fuehrerscheinRepo.findByFuehrerscheinId(fuehrerscheinId))
                .thenReturn(Optional.empty());

        FahrschuelerDTO result = service.createFahrschueler(validDto, mock(HttpServletRequest.class));

        assertNotNull(result);
        assertTrue(result.fuehrerscheine().isEmpty());
    }

    // Duplicate Check
    @Test
    void createFahrschueler_DuplicateCheck_Throws() {
        when(fahrschuelerRepo.existsByNachnameAndGeburtsdatum(anyString(), any(LocalDate.class)))
                .thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.createFahrschueler(validDto, mock(HttpServletRequest.class))
        );

        assertTrue(ex.getMessage().contains("FahrschuelerVorhanden"));
    }
}
