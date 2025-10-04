

package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffTransaction {
    @Id
    private Integer id;  // same as Transaction id

    private BigDecimal staffPaid;

    private BigDecimal ncdmbPaid;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private NCDMBStaff staff;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Transaction transaction;
}

