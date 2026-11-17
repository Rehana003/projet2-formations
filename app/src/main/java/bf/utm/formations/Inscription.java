package bf.utm.formations;

import jakarta.persistence.*;

@Entity
@Table(name = "inscription",
       uniqueConstraints = @UniqueConstraint(name = "uk_formation_email", columnNames = {"formation_id", "email"}))
public class Inscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "formation_id", nullable = false)
    private Long formationId;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String email;

    public Long getId() { return id; }
    public Long getFormationId() { return formationId; }
    public void setFormationId(Long formationId) { this.formationId = formationId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
