package bf.utm.formations;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "formation")
public class Formation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titre;
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    @Column(nullable = false)
    private int places;

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public int getPlaces() { return places; }
    public void setPlaces(int places) { this.places = places; }
}
