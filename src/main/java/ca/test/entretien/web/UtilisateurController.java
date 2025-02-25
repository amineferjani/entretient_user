package ca.test.entretien.web;

import ca.test.entretien.entities.Utilisateur;
import ca.test.entretien.externe.ClientDevise;
import ca.test.entretien.services.UtilisateurService;
import ch.qos.logback.core.net.server.Client;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ca.test.entretien.Utils.Utils.URL_END_POINT;
import static ca.test.entretien.Utils.Utils.URL_PATH;

@RestController
@RequestMapping(URL_PATH)
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    private final ClientDevise clientDevise;

    public UtilisateurController(UtilisateurService utilisateurService, ClientDevise clientDevise) {
        this.utilisateurService = utilisateurService;
        this.clientDevise = clientDevise;
    }
    @GetMapping(URL_END_POINT)
    public ResponseEntity<List<Utilisateur>> getAllUsers(){
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }
    @GetMapping(URL_END_POINT+"/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id){
        return ResponseEntity.ok(utilisateurService.getUserByID(id));
    }
    @PostMapping(URL_END_POINT)
    public ResponseEntity<Utilisateur> addNewUtilisateur(@Valid @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.addNewUtilisateur(utilisateur));
    }
    @PutMapping(URL_END_POINT+"/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id,@Valid @RequestBody Utilisateur utilisateur){
        return ResponseEntity.ok(utilisateurService.updateUserByID(id, utilisateur));
    }
    @DeleteMapping(URL_END_POINT+"/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id){
        utilisateurService.deleteUserByID(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test/{prix}")
    public ResponseEntity<Double> testPrix(@PathVariable Double prix){
        return ResponseEntity.ok(clientDevise.getResult(prix));
    }
}
