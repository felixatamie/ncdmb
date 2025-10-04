
package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
        import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cadre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "ncdmb_percent", precision = 5, scale = 2)
    private BigDecimal ncdmbPercent;

    @Column(name = "staff_percent", precision = 5, scale = 2)
    private BigDecimal staffPercent;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime modifiedAt = LocalDateTime.now();
}

