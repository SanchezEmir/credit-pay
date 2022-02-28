package com.nttdata.creditpay.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreditCard {
  
  private String id;
  
  private String cardNumber;
  
  private Customer customer;
  
  private Double limitCredit;
  
  private LocalDate expiration;
  
  private LocalDateTime createAt;

}
