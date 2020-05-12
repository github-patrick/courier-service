package com.courier.domain.dtos;

import com.courier.domain.enums.DeliveryDriverStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@Builder(builderClassName = "baseBuilder")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryDriverRequestDto {

    @NotBlank(message = "Full name cannot be blank nor empty.")
    private String fullName;
    private DeliveryDriverStatus deliveryDriverStatus;
    private CourierUserResponseDto courierUser;

    @Email
    private String email;

    public DeliveryDriverRequestDto(String fullName, DeliveryDriverStatus deliveryDriverStatus,
                                    CourierUserResponseDto courierUser) {
        this.fullName = fullName;
        this.deliveryDriverStatus = deliveryDriverStatus;
        this.courierUser = courierUser;
    }

    public DeliveryDriverRequestDto(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    @Builder(builderMethodName = "subBuilder")
    public DeliveryDriverRequestDto(String fullName, DeliveryDriverStatus deliveryDriverStatus, String email) {
        this.fullName = fullName;
        this.deliveryDriverStatus = deliveryDriverStatus;
        this.email = email;
    }
}
