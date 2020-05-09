package com.courier.domain.dtos;

import com.courier.domain.enums.UserType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"createdAt","modifiedAt"})
@XmlRootElement(name = "CourierUserResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CourierUserResponseDto {


    private Long id;
    private String email;
    private String password;

    @XmlElementWrapper(name = "types")
    @XmlElement(name = "types")
    private List<UserType> types;
    private Date createdAt;
    private Date modifiedAt;
}
