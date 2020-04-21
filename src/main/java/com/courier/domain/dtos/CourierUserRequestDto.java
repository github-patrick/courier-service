package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CourierUserRequestDto {

    private String email;
    private String password;
    private UserType userType;

}
