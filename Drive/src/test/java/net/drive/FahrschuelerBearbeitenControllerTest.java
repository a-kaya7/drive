package net.drive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.services.fahrschueler.aussensicht.IFahrschuelerBearbeitenService;

@SpringBootTest
@AutoConfigureMockMvc
public class FahrschuelerBearbeitenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IFahrschuelerBearbeitenService service;

    @Test
    void testGetFahrschueler() throws Exception {
        UUID id = UUID.randomUUID();
        Fahrschueler fahrschueler = new Fahrschueler();
        fahrschueler.setVorname("Max");
        fahrschueler.setNachname("Mustermann");

        when(service.getFahrschueler(eq(id))).thenReturn(fahrschueler);

        mockMvc.perform(get("/api/fahrschueler/{fahrschuelerId}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vorname").value("Max"))
                .andExpect(jsonPath("$.nachname").value("Mustermann"));
    }

    @Test
    void testUpdateFahrschueler() throws Exception {
        UUID id = UUID.randomUUID();
        Fahrschueler inputFahrschueler = new Fahrschueler();
        inputFahrschueler.setVorname("Anna");
        inputFahrschueler.setNachname("Müller");

        Fahrschueler updatedFahrschueler = new Fahrschueler();
        updatedFahrschueler.setVorname("Anna");
        updatedFahrschueler.setNachname("Müller");

        when(service.updateFahrschueler(any(Fahrschueler.class), eq(id))).thenReturn(updatedFahrschueler);

        mockMvc.perform(put("/api/fahrschuelerbearbeiten/{fahrschuelerId}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputFahrschueler)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vorname").value("Anna"))
                .andExpect(jsonPath("$.nachname").value("Müller"));
    }
}
