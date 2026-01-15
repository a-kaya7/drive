package net.drive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import net.drive.services.fahrschueler.aussensicht.IFahrschuelerLoeschenService;

@SpringBootTest
@AutoConfigureMockMvc
public class FahrschuelerLoeschenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFahrschuelerLoeschenService service;

    @Test
    void testDeleteFahrschueler() throws Exception {
        UUID fahrschuelerId = UUID.randomUUID();

        
        doNothing().when(service).deleteFahrschueler(any(UUID.class));

        
        mockMvc.perform(delete("/api/fahrschuelerloeschen/{fahrschuelerId}", fahrschuelerId))
                .andExpect(status().isOk());
    }
}
