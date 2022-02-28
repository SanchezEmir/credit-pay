package com.nttdata.creditpay.entity.dto;

import com.nttdata.creditpay.entity.enums.ETypeCustomer;

import lombok.Data;

@Data
public class TypeCustomer {
  
  private String id;
  
  private ETypeCustomer value;
  
  private SubType subType;

}
