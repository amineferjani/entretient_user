package ca.test.entretien.services;

import ca.test.entretien.entities.Utilisateur;
import ca.test.entretien.exceptions.ResourceNotFoundException;
import ca.test.entretien.models.Role;
import ca.test.entretien.repositories.UtilisateurRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {
@Mock
  private  UtilisateurRepo repo;
@InjectMocks
  private  UtilisateurService service;
private Utilisateur utilisateur;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utilisateur = Utilisateur.builder()
                .id(1L).nom("Asma").email("asma@example.com")
                .role(Role.USER).build();
    }

    @Test
    void getAllUsers() {
        //given
        List<Utilisateur> utilisateurs = Arrays.asList(utilisateur);
        when(repo.findAll()).thenReturn(utilisateurs);
        //when
        List<Utilisateur> result = service.getAllUsers();
        //then
        assertNotNull(result);
        assertEquals(utilisateurs.size(), result.size());
        assertTrue(utilisateurs.contains(utilisateur));
        verify(repo,times(1)).findAll();
    }

    @Test
    void getUserByIDWhenExists() {
        //given
        when(repo.findById(1L)).thenReturn(Optional.of(utilisateur));
        //when
        Utilisateur result = service.getUserByID(1L);
        //then
        assertNotNull(result);
        assertEquals(utilisateur.getId(), result.getId());
        assertEquals(utilisateur.getNom(), result.getNom());
        assertEquals(utilisateur.getEmail(), result.getEmail());
        assertEquals(utilisateur.getRole(), result.getRole());
        verify(repo,times(1)).findById(1L);
    }
    @Test
    void getUserByIDWhenDoesNotExists() {
        //given
        when(repo.findById(1L)).thenThrow(new ResourceNotFoundException("Utilisateur with id 1 not found"));
        //when
        //Utilisateur result = service.getUserByID(1L);
        //then
        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class,
                        () -> service.getUserByID(1L));
        verify(repo,times(1)).findById(1L);
        assertEquals("Utilisateur with id 1 not found", resourceNotFoundException.getMessage());
    }

    @Test
    void addNewUtilisateur() {
        //given
        when(repo.save(any(Utilisateur.class))).thenReturn(utilisateur);
        //when
        Utilisateur result= service.addNewUtilisateur(utilisateur);
        //then
        assertNotNull(result);
        assertEquals(utilisateur.getId(), result.getId());
        assertEquals(utilisateur.getNom(), result.getNom());
        assertEquals(utilisateur.getEmail(), result.getEmail());
        assertEquals(utilisateur.getRole(), result.getRole());
        verify(repo,times(1)).save(utilisateur);
    }

    @Test
    void updateUserByIDWhenExists() {
        //given
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.save(any(Utilisateur.class))).thenReturn(utilisateur);
        //when
        Utilisateur result = service.updateUserByID(1L, utilisateur);
        //then
        assertNotNull(result);
        assertEquals(utilisateur, result);
        verify(repo,times(1)).existsById(1L);
        verify(repo,times(1)).save(utilisateur);
    }
    @Test
    void updateUserByIDWhenDoesNotExists() {
        //given
        when(repo.existsById(1L)).thenReturn(false);
        //when
        // then
        ResourceNotFoundException exception=
                assertThrows(ResourceNotFoundException.class,
                        ()->service.updateUserByID(1L, utilisateur));
        verify(repo,times(1)).existsById(1L);
        assertEquals("Utilisateur with id 1 not found", exception.getMessage());
    }

    @Test
    void deleteUserByIDWhenExists() {
        //given
        when(repo.existsById(1L)).thenReturn(true);
        //when
        service.deleteUserByID(1L);
        //then
        verify(repo,times(1)).existsById(1L);
        verify(repo,times(1)).deleteById(1L);
    }
    @Test
    void deleteUserByIDWhenDoesNotExists() {
        //given
        when(repo.existsById(1L)).thenReturn(false);
        //when
        //then
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        ()->service.deleteUserByID(1L));
        assertEquals("Utilisateur with id 1 not found", exception.getMessage());
        verify(repo,times(1)).existsById(1L);
    }
}
