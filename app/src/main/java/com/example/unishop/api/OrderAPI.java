package com.example.unishop.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.unishop.models.Product;
import com.example.unishop.services.OrderListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrderAPI {
    private Context context;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private OrderListener.AddToCartListener addToCartListener;

    public OrderAPI(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart");
    }

    public  void addProductToCart(Product product){
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();

        Map<String, Object> item = new HashMap<>();
        item.put("id", product.getId());
        item.put("name", product.getName());
        item.put("category", product.getCategory());
        item.put("price", product.getPrice());
        item.put("quantity", "1");
        item.put("description", product.getDescription());
        item.put("image", product.getImage());

        databaseReference
                .child(uid)
                .child(timeStamp)
                .setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       addToCartListener.onItemAdded();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       addToCartListener.onFailureResponse(e);
                    }
                });
    }

    public OrderListener.AddToCartListener getAddToCartListener() {
        return addToCartListener;
    }

    public void setAddToCartListener(OrderListener.AddToCartListener addToCartListener) {
        this.addToCartListener = addToCartListener;
    }
}
