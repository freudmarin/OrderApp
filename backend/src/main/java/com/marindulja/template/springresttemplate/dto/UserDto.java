package com.marindulja.template.springresttemplate.dto;

import com.marindulja.template.springresttemplate.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {

    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @NotBlank(message = "Full name is mandatory")
    private String jobTitle;

    private Role role;
}
