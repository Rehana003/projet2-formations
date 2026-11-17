package bf.utm.formations;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    long countByFormationId(Long formationId);
    boolean existsByFormationIdAndEmailIgnoreCase(Long formationId, String email);
    List<Inscription> findByFormationIdOrderByNom(Long formationId);
}
