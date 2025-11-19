package fitness.club.dto.specializationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecializationRequestDto {
    private final String name;
}
