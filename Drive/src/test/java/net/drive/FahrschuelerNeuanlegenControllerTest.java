package net.drive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerNeuanlegenService;

@SpringBootTest
@AutoConfigureMockMvc
public class FahrschuelerNeuanlegenControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFahrschuelerNeuanlegenService service;

    @Test
    void testCreateFahrschueler() throws Exception {
    	
        FahrschuelerDTO dto = new FahrschuelerDTO(
            UUID.randomUUID(),        // fahrschuelerId
            "Max",                    // vorname
            "Mustermann",             // nachname
            LocalDate.of(2000, 1, 1), // geburtsdatum
            null,                     // adresse
            "0123456789",             // telefonnummer
            "max@test.de",            // email
            new HashSet<>(),           // fuehrerscheine
            LocalDate.now(),           // anmeldedatum
            null,                     // pruefungsstatus
            false,                    // bezahlt
            null,                     // dokumente
            null,                     // hinweis
            null,                     // notfallkontakt
            "admin",                  // ersteller
            "mandant1"                // mandant
        );

        // Servis 
        when(service.createFahrschueler(any(FahrschuelerDTO.class), any()))
            .thenReturn(dto);

        
        mockMvc.perform(post("/api/fahrschuelerneuanlage")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vorname\":\"Max\",\"nachname\":\"Mustermann\",\"bezahlt\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vorname").value("Max"))
                .andExpect(jsonPath("$.nachname").value("Mustermann"))
                .andExpect(jsonPath("$.bezahlt").value(false));
    }

}
