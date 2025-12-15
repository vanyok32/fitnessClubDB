package fitness.club.dto.clubDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor@NoArgsConstructor
public class ClubRequestDto {
    private  String address;
    private  String name;
}
