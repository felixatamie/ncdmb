
package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealTicket {
    @Id
    private String id;

    private LocalDateTime issuedAt;

    @OneToOne
    @JoinColumn(name = "transaction_id", unique = true)
    private Transaction transaction;

    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
    }
}

