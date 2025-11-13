package com.ordermanager.api.dto;

import com.ordermanager.api.model.OrderStatus;

public class ChangeStatusRequest {

    private OrderStatus status;

    public ChangeStatusRequest() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

