package com.laszlogulyas.kitchensink_migrated.model;

import lombok.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    @Id
    private String id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 25, message = "Name must be between 1 and 25 characters")
    @Pattern(regexp = "[^0-9]*", message = "Name must not contain numbers")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be a valid email address")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits")
    @Digits(fraction = 0, integer = 12, message = "Phone number must be numeric and up to 12 digits")
    @Field("phone_number")
    private String phoneNumber;
}
