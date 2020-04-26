package com.courier.domain.dtos;

import com.courier.domain.CourierUser;
import com.courier.domain.enums.DeliveryDriverStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DeliveryDriverResponseDto {

    private Long id;
    private String fullName;
    private DeliveryDriverStatus deliveryDriverStatus;
    private CourierUserResponseDto courierUser;
}
