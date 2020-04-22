package com.courier.domain;

import com.courier.domain.enums.UserType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class CourierUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
    private UserType userType;

    public CourierUser(String email, String password, UserType userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
