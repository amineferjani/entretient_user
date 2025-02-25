package ca.test.entretien;

import ca.test.entretien.entities.Utilisateur;
import ca.test.entretien.models.Role;
import ca.test.entretien.services.UtilisateurService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EntretienApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntretienApplication.class, args);
    }
@Bean
    CommandLineRunner init(UtilisateurService utilisateurService) {
        return args -> {
            utilisateurService.addNewUtilisateur(
                    Utilisateur.builder()
                            .nom("John").email("john@example.com")
                            .role(Role.ADMIN)
                            .build()
            );
            utilisateurService.addNewUtilisateur(
                    Utilisateur.builder()
                            .nom("Amine").email("Amine@example.com")
                            .role(Role.MANAGER)
                            .build()
            );
            utilisateurService.addNewUtilisateur(
                    Utilisateur.builder()
                            .nom("Asma").email("asma@example.com")
                            .role(Role.USER)
                            .build()
            );
            utilisateurService.addNewUtilisateur(
                    Utilisateur.builder()
                            .nom("Houda").email("Houda@example.com")
                            .role(Role.USER)
                            .build()
            );
        };
}
}
