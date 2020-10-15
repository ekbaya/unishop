package com.example.unishop.services;

public interface SalesListener {
    interface SaleOrderListener{
        void onOrderPlaced();
        void onFailureResponse(Exception e);
    }
}
