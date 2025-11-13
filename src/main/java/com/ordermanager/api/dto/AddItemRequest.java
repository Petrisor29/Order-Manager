package com.ordermanager.api.dto;

public class AddItemRequest{

  private Long productId;
  private Integer quantity;
  public AddItemRequest(){
  }

  public Long getProductId(){
    return productId;
  }

  public void setProductId(Long productId){
    this.productId = productId;
  }

  public Integer getQuantity(){
    return quantity;
  }

  public void setQuantity(){
    this.quantity = quantity;
  }

}
