package bf.utm.formations;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public final class Dtos {
    private Dtos() {}

    public record FormationRequest(@NotBlank String titre, @NotNull LocalDate dateDebut, @Min(1) int places) {}

    public record InscriptionRequest(@NotBlank String nom, @NotBlank @Email String email) {}

    public record FormationView(Long id, String titre, LocalDate dateDebut, int places,
                                long inscrits, long placesRestantes) {}
}
