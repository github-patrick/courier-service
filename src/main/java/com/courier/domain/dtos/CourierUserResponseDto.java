package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"createdAt","modifiedAt"})
public class CourierUserResponseDto {

    private Long id;
    private String email;
    private String password;
    private UserType userType;
    private Date createdAt;
    private Date modifiedAt;
}
