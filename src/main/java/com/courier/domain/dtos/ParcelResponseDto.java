package com.courier.domain.dtos;

import com.courier.domain.Customer;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParcelResponseDto {
    private String id;
    private String destination;
    private String origin;

    private Priority priority;

    private ParcelStatus status;

    private Date createdAt;
    private Date modifiedAt;


    @JsonIgnore
    private CustomerResponseDto sender;

    @JsonIgnore
    private DeliveryDriverResponseDto deliverer;
}
