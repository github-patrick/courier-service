package com.courier.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"createdAt","modifiedAt"})
@XmlRootElement
public class CustomerRequestDto {

    @NotBlank(message = "Full name cannot be blank nor null")
    private String fullName;

    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourierUserResponseDto courierUser;

}
