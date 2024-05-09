package com.laszlogulyas.kitchensink_migrated.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 25, message = "Name must be between 1 and 25 characters")
    @Pattern(regexp = "[^0-9]*", message = "Name must not contain numbers")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be a valid email address")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits")
    @Digits(fraction = 0, integer = 12, message = "Phone number must be numeric and up to 12 digits")
    @Column(name = "phone_number")
    private String phoneNumber;
}
