package com.example.unishop.api;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.unishop.models.Product;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.SharedHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductsAPI {
    private Context context;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProductsListener.AddItemListener addItemListener;
    private ProductsListener.LoadItemsListener loadItemsListener;
    private ProductsListener.UpdateProductListener updateProductListener;
    List<Product> productList;

    public ProductsAPI(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.keepSynced(true);
        productList = new ArrayList<>();
    }

    public void addItem(byte[] data){
        //for product image name, post-id, post-publish-time
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Products/" + "product_" + timeStamp;

        storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        storageReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to firebase storage now get its url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        String downloadUri = Objects.requireNonNull(uriTask.getResult()).toString();
                        if (uriTask.isSuccessful()) {
                            //url is received upload post to Firebase database
                            Map<Object, String> params = new HashMap<>();
                            params.put("id",timeStamp);
                            params.put("name", SharedHelper.getKey(context, "p_name"));
                            params.put("category", SharedHelper.getKey(context, "category"));
                            params.put("price", SharedHelper.getKey(context, "price"));
                            params.put("quantity", SharedHelper.getKey(context, "quantity"));
                            params.put("description", SharedHelper.getKey(context, "description"));
                            params.put("image", downloadUri);

                            databaseReference.child(timeStamp).setValue(params)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            addItemListener.onItemAdded();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            addItemListener.onFailureAddingItem(e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addItemListener.onFailureUploadingImage(e);
                    }
                });
    }

    public void updateProduct(String id, String image, byte[] data, Map<String, Object> params){
        //product is with image, delete the previous product image first
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //store the new image
                        String timeStamp = String.valueOf(System.currentTimeMillis());
                        String filePathAndName = "Posts/"+ "post_"+timeStamp;

                        storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                        storageReference.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //image updated get its url
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful());

                                        String downloadUri = uriTask.getResult().toString();
                                        if (uriTask.isSuccessful()){
                                            params.put("image", downloadUri);
                                            databaseReference.child(id).updateChildren(params)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            updateProductListener.onProductUpdated();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            updateProductListener.onFailureUpdatingProduct(e);
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        updateProductListener.onFailureUploadingNewImage(e);
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updateProductListener.onFailureDeletingOldImage(e);
                    }
                });
    }

    public void  getAllProducts(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    productList.add(product);
                }
                loadItemsListener.onProductsReceived(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadItemsListener.onDatabaseCancelled(error);

            }
        });
    }

    public void searchProducts(String keyword){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Product product = ds.getValue(Product.class);

                    if (product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            product.getCategory().toLowerCase().contains(keyword.toLowerCase()))
                    {
                        productList.add(product);
                    }
                    loadItemsListener.onProductsReceived(productList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadItemsListener.onDatabaseCancelled(error);

            }
        });

    }

    public ProductsListener.AddItemListener getAddItemListener() {
        return addItemListener;
    }

    public void setAddItemListener(ProductsListener.AddItemListener addItemListener) {
        this.addItemListener = addItemListener;
    }

    public ProductsListener.LoadItemsListener getLoadItemsListener() {
        return loadItemsListener;
    }

    public void setLoadItemsListener(ProductsListener.LoadItemsListener loadItemsListener) {
        this.loadItemsListener = loadItemsListener;
    }

    public ProductsListener.UpdateProductListener getUpdateProductListener() {
        return updateProductListener;
    }

    public void setUpdateProductListener(ProductsListener.UpdateProductListener updateProductListener) {
        this.updateProductListener = updateProductListener;
    }
}
