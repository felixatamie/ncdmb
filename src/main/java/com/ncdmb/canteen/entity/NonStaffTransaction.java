package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonStaffTransaction {
    @Id
    private Integer id; // same as Transaction id

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Transaction transaction;
}

