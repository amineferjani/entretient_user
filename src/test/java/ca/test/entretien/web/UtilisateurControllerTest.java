package ca.test.entretien.web;

import ca.test.entretien.entities.Utilisateur;
import ca.test.entretien.exceptions.ResourceNotFoundException;
import ca.test.entretien.models.Role;
import ca.test.entretien.services.UtilisateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UtilisateurControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UtilisateurService utilisateurService;
    @InjectMocks
    private UtilisateurController utilisateurController;
    Utilisateur utilisateur;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utilisateur = Utilisateur.builder()
                .id(1L).nom("Asma").email("asma@example.com")
                .role(Role.USER).build();
        mockMvc = MockMvcBuilders.standaloneSetup(utilisateurController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers() throws Exception {
        //given
        List<Utilisateur> utilisateurs = Arrays.asList(utilisateur);
        when(utilisateurService.getAllUsers()).thenReturn(utilisateurs);
        //when
        //then
        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(utilisateur.getId()))
                .andExpect(jsonPath("$[0].nom").value(utilisateur.getNom()))
                .andExpect(jsonPath("$[0].email").value(utilisateur.getEmail()))
                .andExpect(jsonPath("$[0].role").value(utilisateur.getRole().toString()));
        verify(utilisateurService,times(1)).getAllUsers();
    }

    @Test
    void getUtilisateurByIdWhenExists() throws Exception {
        //given
        when(utilisateurService.getUserByID(1L)).thenReturn(utilisateur);
        //when
        //then
        mockMvc.perform(get("/api/utilisateurs/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(utilisateur.getId()))
                .andExpect(jsonPath("$.nom").value(utilisateur.getNom()))
                .andExpect(jsonPath("$.email").value(utilisateur.getEmail()))
                .andExpect(jsonPath("$.role").value(utilisateur.getRole().toString()));
        verify(utilisateurService,times(1)).getUserByID(1L);

    }
    @Test
    void getUtilisateurByIdWhenDoesNotExists() throws Exception {
       //given
        when(utilisateurService.getUserByID(1L)).thenThrow(
                new ResourceNotFoundException("Utilisateur with id 1 not found")
        );
       //when
       //then
        mockMvc.perform(get("/api/utilisateurs/{id}",1L))
                .andExpect(status().isNotFound());
        //.andExpect(content().string("Client with id 1 not found"));
        verify(utilisateurService,times(1)).getUserByID(1L);
    }
    @Test
    void addNewUtilisateur()  throws Exception {
        //given
        String clientJson = "{\"nom\":\"Asma\",\"email\":\"asma@example.com\",\"role\":\"USER\"}";
        when(utilisateurService.addNewUtilisateur(any(Utilisateur.class))).thenReturn(utilisateur);
        //when
        //then
        mockMvc.perform(post("/api/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value(utilisateur.getNom()))
                .andExpect(jsonPath("$.email").value(utilisateur.getEmail()))
                .andExpect(jsonPath("$.role").value(utilisateur.getRole().toString()));
        verify(utilisateurService,times(1)).addNewUtilisateur(any(Utilisateur.class));
    }

    @Test
    void updateUtilisateurWhenExists() throws Exception {
        // Given: Un utilisateur existant avec un ID 1L
        Long userId = 1L;
        String clientJson = "{\"nom\":\"Asma\",\"email\":\"asma@example.com\",\"role\":\"USER\"}"; //when(utilisateurService.updateUserByID(userId, any(Utilisateur.class))).thenReturn(utilisateur);
when(utilisateurService.updateUserByID(eq(userId),any(Utilisateur.class))).thenReturn(utilisateur);
        // When & Then: Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(put("/api/utilisateurs/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))  // Convertir en JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nom").value("Asma"))
                .andExpect(jsonPath("$.email").value("asma@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        // Vérifier que le service a été appelé une seule fois
        verify(utilisateurService, times(1)).updateUserByID(eq(userId), any(Utilisateur.class));

    }

    @Test
    void deleteUtilisateur() throws Exception {
        doNothing().when(utilisateurService).deleteUserByID(1L);

        mockMvc.perform(delete("/api/utilisateurs/{id}",1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
