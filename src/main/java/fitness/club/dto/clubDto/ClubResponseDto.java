package fitness.club.dto.clubDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor@NoArgsConstructor
public class ClubResponseDto {
    private  Integer id;
    private  String address;
    private  String name;
}
