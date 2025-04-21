package com.example.apisample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "point", nullable = false)
    private Integer point;

    @NotNull
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String description;

    @Column(name = "create_on")
    private Instant createOn;

    @Column(name = "last_update_on")
    private Instant lastUpdateOn;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

}