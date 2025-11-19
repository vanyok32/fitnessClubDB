package fitness.club.dto.clinetDto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ClientResponseDto {
    private final Integer id;
    private final Integer clubId;
    private final String name;
    private final String email;
}
