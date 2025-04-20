package com.example.apisample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    @Size(max = 100)
    @Column(name = "image", length = 100)
    private String image;

    @NotNull
    @Column(name = "is_featured", nullable = false)
    private Boolean featured = Boolean.FALSE;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "create_on")
    private Instant createOn;

    @Column(name = "last_update_on")
    private Instant lastUpdateOn;

}