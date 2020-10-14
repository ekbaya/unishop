package com.example.unishop.api;

import androidx.annotation.NonNull;

import com.example.unishop.models.Category;
import com.example.unishop.services.CategoriesListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesAPI {
    private List<Category> categoryList;
    private CategoriesListener.LoadCategoriesListener loadCategoriesListener;
    private CategoriesListener.AddCategoryListener addCategoryListener;
    private DatabaseReference databaseReference;

    public CategoriesAPI() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        categoryList = new ArrayList<>();
    }

    public void addCategory(String name , String description){
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        Map<Object, String> params = new HashMap<>();
        params.put("id", timeStamp);
        params.put("name", name);
        params.put("description", description);
        databaseReference.child(timeStamp).setValue(params)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addCategoryListener.onCategoryAdded();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addCategoryListener.onFailure(e);
                    }
                });
    }

    public void getCategories(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Category category = ds.getValue(Category.class);
                    categoryList.add(category);
                }
                loadCategoriesListener.onCategoriesReceived(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadCategoriesListener.onDatabaseCancelled(error);
            }
        });

    }

    public CategoriesListener.LoadCategoriesListener getLoadCategoriesListener() {
        return loadCategoriesListener;
    }

    public void setLoadCategoriesListener(CategoriesListener.LoadCategoriesListener loadCategoriesListener) {
        this.loadCategoriesListener = loadCategoriesListener;
    }

    public CategoriesListener.AddCategoryListener getAddCategoryListener() {
        return addCategoryListener;
    }

    public void setAddCategoryListener(CategoriesListener.AddCategoryListener addCategoryListener) {
        this.addCategoryListener = addCategoryListener;
    }
}
