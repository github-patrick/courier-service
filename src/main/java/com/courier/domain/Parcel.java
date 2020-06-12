package com.courier.domain;

import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = {"sender","deliverer"})
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "baseBuilder")
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DELIVERY_DRIVER_ID")
    private DeliveryDriver deliverer;

    @Builder(builderMethodName = "superBuilder")
    public Parcel(Date createdAt, Date modifiedAt, String id, String destination, String origin, Priority priority,
                  ParcelStatus status, Customer sender, DeliveryDriver deliverer) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.destination = destination;
        this.origin = origin;
        this.priority = priority;
        this.status = status;
        this.sender = sender;
        this.deliverer = deliverer;
    }
}
