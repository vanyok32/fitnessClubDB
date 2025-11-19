package fitness.club.dto.coachDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoachResponseDto {
    private final Integer id;
    private final Integer clubId;
    private final String name;
    private final String email;
}
