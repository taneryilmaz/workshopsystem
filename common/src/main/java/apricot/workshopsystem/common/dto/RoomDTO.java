package apricot.workshopsystem.common.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class RoomDTO {
    private Long id;

    @NotEmpty(message = "Name is mandatory")
    private String name;

    @Positive
    private int numberOfSeat;
}
