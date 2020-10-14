package com.example.unishop.services;

import com.example.unishop.models.Product;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface OrderListener {
    interface AddToCartListener{
        void onItemAdded();
        void onFailureResponse(Exception e);
    }

    interface LoadOrdersListener{
        void onOrdersReceived(List<Product> orderList);
        void onDatabaseCancelled(DatabaseError error);
    }
}
