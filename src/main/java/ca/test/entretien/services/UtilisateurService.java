package ca.test.entretien.services;

import ca.test.entretien.entities.Utilisateur;
import ca.test.entretien.exceptions.ResourceNotFoundException;
import ca.test.entretien.repositories.UtilisateurRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    private final UtilisateurRepo repo;

    public UtilisateurService(UtilisateurRepo repo) {
        this.repo = repo;
    }

    public List<Utilisateur> getAllUsers() {
        return repo.findAll();
    }

    public Utilisateur getUserByID(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur with id " + id + " found"));
    }
    public Utilisateur addNewUtilisateur(Utilisateur utilisateur) {
        return repo.save(utilisateur);
    }

    public Utilisateur updateUserByID(Long id, Utilisateur utilisateur) {
        if (repo.existsById(id)) {
            utilisateur.setId(id);
            return repo.save(utilisateur);
        } else throw new ResourceNotFoundException("Utilisateur with id " + id + " not found");
    }
    public void deleteUserByID(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else throw new ResourceNotFoundException("Utilisateur with id " + id + " not found");
    }
}
