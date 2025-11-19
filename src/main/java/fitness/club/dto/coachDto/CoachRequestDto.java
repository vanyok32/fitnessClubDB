package fitness.club.dto.coachDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CoachRequestDto {
    private final Integer clubId;
    private final String name;
    private final String email;
}
