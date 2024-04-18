package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserLoginDTO {
    @JsonProperty("email")
    @NotBlank(message = "email is required")
    @Email(message = "Email must be in correct email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
