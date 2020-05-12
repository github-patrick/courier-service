package com.courier.domain.dtos;

import com.courier.domain.enums.DeliveryDriverStatus;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryDriverResponseDto {

    private Long id;
    private String fullName;
    private DeliveryDriverStatus deliveryDriverStatus;
    private CourierUserResponseDto courierUser;
    private Date createdAt;
    private Date modifiedAt;
}
