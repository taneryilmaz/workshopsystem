package apricot.workshopsystem.entityservice.model.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Positive
    @Column
    private long roomId;

    @Positive
    @Column
    private long bookerId;

    @Column(name = "startDateTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDateTime;

    @Column(name = "endDateTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime endDateTime;
}
