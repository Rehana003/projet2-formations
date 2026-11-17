package bf.utm.formations;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formations")
public class FormationController {
    private final FormationService service;

    public FormationController(FormationService service) { this.service = service; }

    @GetMapping
    public List<Dtos.FormationView> lister() { return service.lister(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.FormationView ajouter(@Valid @RequestBody Dtos.FormationRequest r) { return service.ajouter(r); }

    @GetMapping("/{id}/inscriptions")
    public List<Inscription> inscrits(@PathVariable Long id) { return service.inscrits(id); }

    @PostMapping("/{id}/inscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public Inscription inscrire(@PathVariable Long id, @Valid @RequestBody Dtos.InscriptionRequest r) {
        return service.inscrire(id, r);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String, String>> statut(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> invalide(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(Map.of("message", "Données invalides : vérifiez les champs saisis"));
    }

    // Filet de sécurité : la contrainte unique (formation_id, email) de la base
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String, String>> doublon(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Cet étudiant est déjà inscrit à cette formation"));
    }
}
