package com.hamid.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Name can not be blank")
    private String name;

    @Email(message = "Email is Invalid")
    @NotBlank(message = "Email is required to update")
    private String email;

    @Size(min = 6, message = "Password Must Be Atleast 6 Characters: ")
    private String password;

}
