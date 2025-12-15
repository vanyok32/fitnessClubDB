package fitness.club.dto.clinetDto;

import lombok.*;

@Data
@NoArgsConstructor@AllArgsConstructor
@Builder
public class ClientResponseDto {
    private  Integer id;
    private  Integer clubId;
    private  String name;
    private  String email;
    private  String password;
}
