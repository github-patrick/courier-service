package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
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
    private String password;

    @NotNull(message = "Courier account type cannot be null.")
    @XmlElementWrapper(name = "types")
    @XmlElement(name = "types")
    private List<UserType> types;

}
