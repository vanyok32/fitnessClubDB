package fitness.club.dto.clubDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubRequestDto {
    private final String address;
    private final String name;
}
