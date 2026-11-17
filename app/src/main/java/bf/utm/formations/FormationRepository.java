package bf.utm.formations;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Formation f where f.id = :id")
    Optional<Formation> findByIdForUpdate(Long id);
}
