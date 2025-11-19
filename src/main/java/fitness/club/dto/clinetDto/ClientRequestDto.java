package fitness.club.dto.clinetDto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ClientRequestDto {
    private final Integer clubId;
    private final String name;
    private final String email;
}
