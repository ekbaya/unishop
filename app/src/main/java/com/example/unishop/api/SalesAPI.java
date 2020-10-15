package com.example.unishop.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.unishop.models.Product;
import com.example.unishop.services.SalesListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SalesAPI {
    private Context context;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private SalesListener.SaleOrderListener saleOrderListener;

    public SalesAPI(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference("Sales");
    }

    public void sellOrders(List<Product> orderList){
        String timestamp = String.valueOf(System.currentTimeMillis());
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        reference.child(uid).child(timestamp)
                .setValue(orderList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           saleOrderListener.onOrderPlaced();
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        saleOrderListener.onFailureResponse(e);

                    }
                });
    }

    public SalesListener.SaleOrderListener getSaleOrderListener() {
        return saleOrderListener;
    }

    public void setSaleOrderListener(SalesListener.SaleOrderListener saleOrderListener) {
        this.saleOrderListener = saleOrderListener;
    }
}
