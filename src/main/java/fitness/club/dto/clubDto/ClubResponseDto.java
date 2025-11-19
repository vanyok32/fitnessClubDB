package fitness.club.dto.clubDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClubResponseDto {
    private final Integer id;
    private final String address;
    private final String name;
}
