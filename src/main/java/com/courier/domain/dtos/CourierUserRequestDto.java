package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import lombok.*;


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
