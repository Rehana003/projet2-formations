package bf.utm.formations;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class FormationService {
    private final FormationRepository formations;
    private final InscriptionRepository inscriptions;

    public FormationService(FormationRepository formations, InscriptionRepository inscriptions) {
        this.formations = formations;
        this.inscriptions = inscriptions;
    }

    public List<Dtos.FormationView> lister() {
        return formations.findAll().stream().map(this::vue).toList();
    }

    @Transactional
    public Dtos.FormationView ajouter(Dtos.FormationRequest r) {
        Formation f = new Formation();
        f.setTitre(r.titre().trim());
        f.setDateDebut(r.dateDebut());
        f.setPlaces(r.places());
        return vue(formations.save(f));
    }

    @Transactional
    public Inscription inscrire(Long formationId, Dtos.InscriptionRequest r) {
        // Verrou sur la formation : évite de dépasser le nombre de places en cas d'inscriptions simultanées
        Formation f = formations.findByIdForUpdate(formationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formation introuvable"));
        String email = r.email().trim().toLowerCase();
        if (inscriptions.existsByFormationIdAndEmailIgnoreCase(formationId, email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet étudiant est déjà inscrit à cette formation");
        if (inscriptions.countByFormationId(formationId) >= f.getPlaces())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Formation complète");
        Inscription i = new Inscription();
        i.setFormationId(formationId);
        i.setNom(r.nom().trim());
        i.setEmail(email);
        return inscriptions.save(i);
    }

    public List<Inscription> inscrits(Long formationId) {
        if (!formations.existsById(formationId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Formation introuvable");
        return inscriptions.findByFormationIdOrderByNom(formationId);
    }

    private Dtos.FormationView vue(Formation f) {
        long n = inscriptions.countByFormationId(f.getId());
        return new Dtos.FormationView(f.getId(), f.getTitre(), f.getDateDebut(), f.getPlaces(), n, f.getPlaces() - n);
    }
}
