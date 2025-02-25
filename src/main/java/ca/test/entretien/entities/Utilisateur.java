package ca.test.entretien.entities;

import ca.test.entretien.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Utilisateur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Le nom ne doit pas etre null")
    private String nom;
    @Email(message = "Erreur format Email???git ")
    @NotNull(message = "Le mail ne doit pas etre null")
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    private Departement departement;
}
