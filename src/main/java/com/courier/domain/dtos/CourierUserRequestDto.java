package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@XmlRootElement
public class CourierUserRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,message = "Password should be greater than 5 characters")
    private String password;
    private UserType userType;

}
