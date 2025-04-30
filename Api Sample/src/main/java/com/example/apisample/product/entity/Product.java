package com.example.apisample.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    @Size(max = 200)
    @Column(name = "image", length = 200)
    private String image;

    @NotNull
    @Column(name = "is_featured", nullable = false)
    private Boolean featured = Boolean.FALSE;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "create_on")
    private Instant createOn;

    @UpdateTimestamp
    @Column(name = "last_update_on")
    private Instant lastUpdateOn;

    @Version
    @Column(name = "version")
    private Long version;

}