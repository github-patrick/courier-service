package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@XmlRootElement(name = "CourierUserRequestDto")
@XmlAccessorType(XmlAccessType.FIELD) //to bind all non static, non transient fields.
public class CourierUserRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,message = "Password should be greater than 5 characters")
    private String password;


    @XmlElementWrapper(name = "types")
    @XmlElement(name = "types")
    private List<UserType> types;

}
