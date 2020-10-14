package com.example.unishop.services;

public interface OrderListener {
    interface AddToCartListener{
        void onItemAdded();
        void onFailureResponse(Exception e);
    }
}
