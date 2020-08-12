package com.example.unishop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.unishop.R;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.utilities.SharedHelper;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, ProductsListener.AddItemListener {
    @BindView(R.id.price) EditText price;
    @BindView(R.id.quantity) EditText quantity;
    @BindView(R.id.p_description) EditText p_description;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.submit) Button submit;

    // permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //arrays of permission to be request
    String[] cameraPermissions;
    String[] storagePermissions;

    //url of picked
    Uri image_uri;
    Bitmap bitmap;
    String string_image;

    private ProductsAPI productsAPI;
    private Loader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);


        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageView.setOnClickListener(this);
        submit.setOnClickListener(this);

        productsAPI = new ProductsAPI(this);
        productsAPI.setAddItemListener(this);

        loader = new Loader(this);
    }

    private void pickImage() {
        if (!checkStoragePermission()){
            requestStoragePermission();
        }
        else {
            pickFromGallery();
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(price.getText().toString())){
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(quantity.getText().toString())){
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (TextUtils.isEmpty(p_description.getText().toString())){
            Toast.makeText(this, "Invalid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (imageView.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.add_image).getConstantState()){
            Toast.makeText(this, "Please add a product photo to upload", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            SharedHelper.putKey(AddProductActivity.this, "price", price.getText().toString());
            SharedHelper.putKey(AddProductActivity.this, "quantity", quantity.getText().toString());
            SharedHelper.putKey(AddProductActivity.this, "description", p_description.getText().toString());
            return true;
        }
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddProductActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
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
    public void onClick(View v) {
        if (v.equals(imageView)){
            pickImage();
        }
        if (v.equals(submit)){
            if (validate()){
                //get Image from imageview
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //image compress
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                productsAPI.addItem(data);
                loader.showDialogue();
            }
        }
    }

    @Override
    public void onItemAdded() {
        loader.hideDialogue();
        Toasty.success(AddProductActivity.this, "Item added successfully...", Toasty.LENGTH_LONG).show();
        startActivity(new Intent(new Intent(AddProductActivity.this, AdminHomeActivity.class)));
        finish();
    }

    @Override
    public void onFailureAddingItem(Exception e) {
        loader.hideDialogue();
        Toasty.error(AddProductActivity.this, "Failed to add item..."+e.getMessage(), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onFailureUploadingImage(Exception e) {
        loader.hideDialogue();
        Toasty.error(AddProductActivity.this, "Failed to upload Image..."+e.getMessage(), Toasty.LENGTH_LONG).show();

    }
}
