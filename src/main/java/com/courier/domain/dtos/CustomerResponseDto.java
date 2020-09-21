package com.courier.domain.dtos;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"createdAt","modifiedAt"})
public class CustomerResponseDto {

    private Long id;
    private String fullName;
    private CourierUserResponseDto courierUser;
    private Date createdAt;
    private Date modifiedAt;
}
