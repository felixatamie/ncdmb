
package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(
        indexes = {
                @Index(name = "idx_transaction_canteen_id", columnList = "canteen_id")
        }
)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    @Column(unique = true)
    private String ticketId;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private CanteenUser operator;

    @ManyToOne
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

/*
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private CanteenUser operator;

 */

