package fitness.club.dto.specializationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecializationResponseDto {
    private final Integer id;
    private final String name;
}
