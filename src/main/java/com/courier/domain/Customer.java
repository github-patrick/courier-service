package com.courier.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @OneToOne
    @JoinColumn(name = "COURIER_USER_ID")
    @NotNull
    private CourierUser courierUser;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private Set<Parcel> parcels = new HashSet<>();
}
