package com.parkhomovsky.bookstore.dto.user;

import com.parkhomovsky.bookstore.validation.FieldsValueMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldsValueMatch(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
    @NotBlank
    @Length(min = 8, max = 50)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
