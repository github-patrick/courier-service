package com.courier.domain.dtos;

import com.courier.domain.enums.DeliveryDriverStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDriverStatusDto {

    @NotNull
    private DeliveryDriverStatus deliveryDriverStatus;
}
