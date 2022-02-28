package com.nttdata.creditpay.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nttdata.creditpay.entity.dto.Credit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection = "credit_transaction")
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransaction {
  
  @Id
  private String id;
  
  @NotNull
  private Credit credit;
  
  @NotBlank
  private String transactionCode;
  
  @NotNull
  private Double transactionAmount;
  
  private LocalDateTime transactionDate;

}
