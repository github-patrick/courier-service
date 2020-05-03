package com.courier.domain;

import com.courier.domain.enums.UserType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder(builderClassName = "baseBuilder")
public class CourierUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder(builderMethodName = "superBuilder")
    public CourierUser(Date createdAt, Date modifiedAt, Long id, String email, String password, UserType userType) {
        super(createdAt,modifiedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
