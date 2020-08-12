package com.example.unishop.services;

import com.example.unishop.models.Category;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface CategoriesListener {
    interface LoadCategoriesListener{
        void onCategoriesReceived(List<Category> categoryList);
        void onDatabaseCancelled(DatabaseError error);
    }

    interface AddCategoryListener{
        void onCategoryAdded();
        void onFailure(Exception e);
    }
}
