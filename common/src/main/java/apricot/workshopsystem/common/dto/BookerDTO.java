package apricot.workshopsystem.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookerDTO {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @NotBlank(message = "Email is mandatory")
    private String email;
}
