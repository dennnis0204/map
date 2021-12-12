package com.desiredchargingstation.map.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name="charging_points")
public class ChargingPoint extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "charging_points_id_seq")
    @SequenceGenerator(name  = "charging_points_id_seq", sequenceName = "charging_points_id_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "latitude is mandatory")
    @Column(precision = 6, scale = 4)
    private BigDecimal latitude;

    @NotNull(message = "longitude is mandatory")
    @Column(precision = 6, scale = 4)
    private BigDecimal longitude;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargingPoint that = (ChargingPoint) o;
        return id.equals(that.id) &&
                latitude.equals(that.latitude) &&
                longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }
}
