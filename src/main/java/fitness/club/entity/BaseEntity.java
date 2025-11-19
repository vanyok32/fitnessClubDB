package fitness.club.entity;

import fitness.club.util.Column;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor

public abstract class BaseEntity {
    @Column(name = "id")
    protected Integer id;
}
