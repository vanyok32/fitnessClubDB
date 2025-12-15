package fitness.club.dto.clinetDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDto {
    private  Integer clubId;
    private  String name;
    private  String email;
    private  String password;
}
