package com.nttdata.creditpay.entity.dto;

import com.nttdata.creditpay.entity.enums.ESubType;

import lombok.Data;

@Data
public class SubType {
  
  private String id;
  
  private ESubType value;

}
