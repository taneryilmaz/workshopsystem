package apricot.workshopsystem.common.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class BookerDTO {
    private Long id;

    @NotEmpty(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Surname is mandatory")
    private String surname;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "${validatedValue} is not a valid email")
    private String email;
}
