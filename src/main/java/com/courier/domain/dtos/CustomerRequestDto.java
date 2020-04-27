package com.courier.domain.dtos;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"createdAt","modifiedAt"})
public class CustomerRequestDto {

    private String fullName;
    private CourierUserResponseDto courierUser;

}
