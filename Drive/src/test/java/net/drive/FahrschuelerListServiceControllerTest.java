package net.drive;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;

@SpringBootTest
@AutoConfigureMockMvc
public class FahrschuelerListServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFahrschuelerListService service;

    @Test
    void testGetAllFahrschueler() throws Exception {
        // Test
        List<FahrschuelerListDTO> list = List.of(
            new FahrschuelerListDTO(
                UUID.randomUUID(),
                "Mustermann",
                LocalDate.of(2000, 1, 1),
                "0123456789",
                new HashSet<>(List.of("B")),
                true,
                "Mandant1"
            ),
            new FahrschuelerListDTO(
                UUID.randomUUID(),
                "Müller",
                LocalDate.of(1995, 5, 10),
                "0987654321",
                new HashSet<>(List.of("A", "BE")),
                false,
                "Mandant2"
            )
        );

        
        when(service.getAllFahrschueler()).thenReturn(list);

        
        mockMvc.perform(get("/api/fahrschuelerlist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nachname").value("Mustermann"))
                .andExpect(jsonPath("$[0].bezahlt").value(true))
                .andExpect(jsonPath("$[1].nachname").value("Müller"))
                .andExpect(jsonPath("$[1].fuehrerscheine.length()").value(2))
                .andExpect(jsonPath("$[1].bezahlt").value(false));
    }
}
