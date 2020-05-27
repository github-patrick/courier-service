package com.courier.domain;

import com.courier.domain.enums.DeliveryDriverStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryDriver extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String fullName;

    @Enumerated(EnumType.STRING)
    private DeliveryDriverStatus deliveryDriverStatus;

    @OneToOne
    @JoinColumn(name = "COURIER_USER_ID")
    private CourierUser courierUser;

    @OneToMany(mappedBy = "deliverer")
    private Set<Parcel> parcels = new HashSet<>();
}
