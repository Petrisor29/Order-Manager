package com.ordermanager.api.dto;

public class CreateOrderRequest{
  private Long CustomerId;

  public CreateOrderRequest(){
  }

  public Long getCustomerId(){
    return customerId;
  }

  public void setCustomerId(){
    this.customerId = customerId;
  
