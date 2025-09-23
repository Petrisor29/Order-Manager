package com.ordermanager.api.model;
public class OrderItem {
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public OrderItem(int productId, String productName, double unitPrice, int quantity){
        this.productId = productId; this.productName = productName;
        this.unitPrice = unitPrice; this.quantity = quantity;
    }
    public int getProductId(){ return productId; }
    public String getProductName(){ return productName; }
    public double getUnitPrice(){ return unitPrice; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int q){ this.quantity = q; }
}
