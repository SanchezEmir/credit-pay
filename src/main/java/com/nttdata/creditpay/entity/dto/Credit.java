package com.nttdata.creditpay.entity.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
  
  private String id;
  
  private CreditCard creditCard;
  
  private Double amount;
  
  private LocalDateTime createdAt;

}
