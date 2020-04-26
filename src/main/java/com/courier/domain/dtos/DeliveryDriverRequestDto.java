package com.courier.domain.dtos;

import com.courier.domain.CourierUser;
import com.courier.domain.enums.DeliveryDriverStatus;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryDriverRequestDto {
    private String fullName;
    private DeliveryDriverStatus deliveryDriverStatus;
    private CourierUserResponseDto courierUser;
}
