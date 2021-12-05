package com.desiredchargingstation.map.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingPoint> chargingPoints = new ArrayList<>();

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints.stream()
                .sorted(Comparator.comparing(ChargingPoint::getId))
                .collect(Collectors.toList());
    }

    public void addChargingPoint(ChargingPoint chargingPoint) {
        chargingPoints.add(chargingPoint);
        chargingPoint.setUser(this);
    }

    public void removeChargingPoint(ChargingPoint chargingPoint) {
        chargingPoints.removeIf(point -> point.getId().equals(chargingPoint.getId()));
        chargingPoint.setUser(null);
    }

    public void updateChargingPoint(ChargingPoint chargingPoint) {
        removeChargingPoint(chargingPoint);
        addChargingPoint(chargingPoint);
    }

    public boolean hasChargingPoint(ChargingPoint chargingPoint) {
        return chargingPoints.stream()
                .filter(point -> point.getId().equals(chargingPoint.getId()))
                .findFirst().isPresent();
    }
}
