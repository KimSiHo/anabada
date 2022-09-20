package me.blog.anabada.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpForm {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    @Length(min = 3, max = 20)
    @NotBlank
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @Length(min = 8, max = 50)
    @NotBlank
    private String password;
}
