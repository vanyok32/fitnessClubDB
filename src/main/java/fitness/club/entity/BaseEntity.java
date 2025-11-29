package fitness.club.entity;

import fitness.club.util.Column;
import lombok.*;

@Data

@RequiredArgsConstructor

public abstract class BaseEntity {
    @Column(name = "id")
    protected Integer id;
}
