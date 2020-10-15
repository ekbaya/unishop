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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAPI {
    private Context context;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private OrderListener.AddToCartListener addToCartListener;
    private OrderListener.LoadOrdersListener loadOrdersListener;
    private List<Product> orderList;
    private OrderListener.RemoveItemsListener removeItemsListener;

    public OrderAPI(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart");
        orderList = new ArrayList<>();
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

    public void getOrders(){
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        databaseReference
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Product order = ds.getValue(Product.class);
                    orderList.add(order);
                }
                loadOrdersListener.onOrdersReceived(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadOrdersListener.onDatabaseCancelled(error);
            }
        });

    }

    public void removeItemsInCart(){
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        databaseReference.child(uid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            removeItemsListener.onItemsRemoved();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        removeItemsListener.onFailureResponse(e);
                    }
                });
    }

    public OrderListener.AddToCartListener getAddToCartListener() {
        return addToCartListener;
    }

    public void setAddToCartListener(OrderListener.AddToCartListener addToCartListener) {
        this.addToCartListener = addToCartListener;
    }

    public OrderListener.LoadOrdersListener getLoadOrdersListener() {
        return loadOrdersListener;
    }

    public void setLoadOrdersListener(OrderListener.LoadOrdersListener loadOrdersListener) {
        this.loadOrdersListener = loadOrdersListener;
    }

    public OrderListener.RemoveItemsListener getRemoveItemsListener() {
        return removeItemsListener;
    }

    public void setRemoveItemsListener(OrderListener.RemoveItemsListener removeItemsListener) {
        this.removeItemsListener = removeItemsListener;
    }
}
