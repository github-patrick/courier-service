package com.courier.domain.dtos;

import com.courier.domain.enums.Priority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParcelRequestDto {

    @NotBlank(message = "City destination must be specified.")
    private String destination;

    @NotBlank(message = "City origin must be specified.")
    private String origin;

    private Priority priority;

    private CustomerResponseDto sender;

    private DeliveryDriverResponseDto deliverer;
}
