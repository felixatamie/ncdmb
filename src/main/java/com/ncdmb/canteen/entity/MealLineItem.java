package com.ncdmb.canteen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
    /*
    {
  "totalAmount": 3500,
  "ticketId": "TXN123456",
  "operatorId": 5,
  "mealLineItems": [
    {
      "mealId": 1,
      "quantity": 2,
      "pricePerUnit": 1000
    },
    {
      "mealId": 2,
      "quantity": 1,
      "pricePerUnit": 1500
    }
  ]
}

     */
}

/*
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private CanteenUser operatorName;
  private Canteen canteenName;


 */
