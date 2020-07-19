package com.example.unishop.services;

import com.example.unishop.models.Product;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface ProductsListener {
    interface AddItemListener{
        void onItemAdded();
        void onFailureAddingItem(Exception e);
        void onFailureUploadingImage(Exception e);
    }
    interface LoadItemsListener{
        void onProductsReceived(List<Product> productList);
        void onDatabaseCancelled(DatabaseError error);
    }

    interface UpdatePriceListener{
        void onPriceUpdated();
        void onDatabaseCancelled(DatabaseError error);
    }

    interface UpdateQuantityListener{
        void onQuantityUpdated();
        void onDatabaseCancelled(DatabaseError error);
    }
}
