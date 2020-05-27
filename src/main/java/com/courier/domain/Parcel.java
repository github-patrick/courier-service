package com.courier.domain;

import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id","cityDestination","priority"})
@Entity
public class Parcel extends BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",
            strategy = "uuid")
    private String id;
    private String destination;

    private String origin;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private ParcelStatus status = ParcelStatus.NOT_DISPATCHED;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "DELIVERY_DRIVER_ID")
    private DeliveryDriver deliverer;
}
