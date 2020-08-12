package com.example.unishop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.unishop.R;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener , ProductsListener.UpdateProductListener {
    //views
    private EditText editTextTextName;
    private EditText editTextTextQuantity;
    private EditText editTextPrice;
    private EditText editTextDescription;
    private Button updateBtn;
    private ImageView imageView, editImage;

    private Intent intent;

    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;

    //arrays of permission to be request
    String[] storagePermissions;
    Uri image_uri;

    private ProductsAPI productsAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        intent = getIntent();
        loader = new Loader(this);
        productsAPI = new ProductsAPI(this);
        productsAPI.setUpdateProductListener(this);

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init
        editTextTextName = findViewById(R.id.editTextTextName);
        editTextTextQuantity = findViewById(R.id.editTextTextQuantity);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        updateBtn = findViewById(R.id.updateBtn);
        imageView = findViewById(R.id.imageView);
        editImage = findViewById(R.id.editImage);

        imageView.setOnClickListener(this);
        editImage.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        //set data
        editTextTextName.setText(intent.getStringExtra("name"));
        editTextTextQuantity.setText(intent.getStringExtra("quantity"));
        editTextPrice.setText(intent.getStringExtra("price"));
        editTextDescription.setText(intent.getStringExtra("description"));

        try {
            Picasso.get().load(intent.getStringExtra("image")).into(imageView);
        }catch (Exception e){
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(imageView)){
            pickImage();

        }
        if (view.equals(editImage)){
            pickImage();
        }
        if (view.equals(updateBtn)){
            if (validated()){
                //get Image from imageview
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //image compress
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();


                String pid = intent.getStringExtra("productID");
                String name = editTextTextName.getText().toString();
                String description = editTextDescription.getText().toString();
                String price = editTextPrice.getText().toString();
                String quantity = editTextTextQuantity.getText().toString();
                String category = intent.getStringExtra("category");
                String image = intent.getStringExtra("image");

                Map<String, Object> params = new HashMap<>();
                params.put("id",pid);
                params.put("name", name);
                params.put("category", category);
                params.put("price", price);
                params.put("quantity", quantity);
                params.put("description", description);

                //calling update product controller
                productsAPI.updateProduct(pid, image, data, params);
                loader.showDialogue();
            }
        }
    }

    private void pickImage() {
        if (!checkStoragePermission()){
            requestStoragePermission();
        }
        else {
            pickFromGallery();
        }
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @SuppressLint("NewApi")
    private void requestStoragePermission() {
        //request runtime storage permission
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean validated() {
        if (TextUtils.isEmpty(editTextTextName.getText().toString())){
            Toasty.warning(UpdateProductActivity.this, "Product name cannot be empty", Toasty.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(editTextTextQuantity.getText().toString())){
            Toasty.warning(UpdateProductActivity.this, "Product quantity cannot be empty", Toasty.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(editTextPrice.getText().toString())){
            Toasty.warning(UpdateProductActivity.this, "Product price cannot be empty", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(editTextDescription.getText().toString())){
            Toasty.warning(UpdateProductActivity.this, "Product description cannot be empty", Toasty.LENGTH_LONG).show();
            return false;
        }else if (imageView.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.add_image).getConstantState()){
            Toast.makeText(this, "Please add a product photo to upload", Toast.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when the user press Allow or Deny from permission request dialog
         * Im handling permission cases (allowed or denied)*/
        if (requestCode == STORAGE_REQUEST_CODE) {// picking from, gallery first check if storage are allowed or not
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    //Permission enabled
                    pickFromGallery();
                } else {
                    //permission denied
                    Toast.makeText(this, "Please enable storage permission ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //This method will be called after picking image form camera or gallery
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                // image is picked from gallery, get uri of image
                //image is picked from gallery, get the uri of image
                image_uri = data.getData();
                //set to image view
                imageView.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductUpdated() {
        loader.hideDialogue();
        Toasty.success(this, "Product updated successfully", Toasty.LENGTH_LONG).show();
        startActivity(new Intent(UpdateProductActivity.this, UpdateItemsActivity.class));
        finish();
    }

    @Override
    public void onFailureDeletingOldImage(Exception e) {
        loader.hideDialogue();
        Toasty.error(this, "Failed to delete old product Image, "+e.getMessage(), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onFailureUploadingNewImage(Exception e) {
        loader.hideDialogue();
        Toasty.error(this, "Failed to upload new product Image, "+e.getMessage(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onFailureUpdatingProduct(Exception e) {
        loader.hideDialogue();
        Toasty.error(this, "Failed to update product, "+e.getMessage(), Toasty.LENGTH_LONG).show();
    }
}