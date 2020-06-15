package com.example.unishop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.utilities.SharedHelper;
import com.example.unishop.models.ModelProduct;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, ProductsListener {
    @BindView(R.id.price) EditText price;
    @BindView(R.id.quantity) EditText quantity;
    @BindView(R.id.p_description) EditText p_description;
    @BindView(R.id.p_image) TextView p_image;
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

        p_image.setOnClickListener(this);
        submit.setOnClickListener(this);

        productsAPI = new ProductsAPI(this);
        productsAPI.setProductsListener(this);

        loader = new Loader(this);
    }

    private void pickImage() {
        showImagePicDialog();
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
        else if (p_image.getText().toString().equals("Click to select an image")){
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            SharedHelper.putKey(AddProductActivity.this, "price", price.getText().toString());
            SharedHelper.putKey(AddProductActivity.this, "quantity", quantity.getText().toString());
            SharedHelper.putKey(AddProductActivity.this, "description", p_description.getText().toString());
            SharedHelper.putKey(AddProductActivity.this, "image_url", string_image);
            return true;
        }
    }
    private void showImagePicDialog() {
        //show dialog containing options camera and gallery to pick the image
        // options to show in dialog
        String options[] = {"Camera", "Gallery"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set tittle
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items clicks
                if (which == 0) {
                    //Camera clicked

                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //Gallery clicked

                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();

        /*for picking image from camera:
         * camera [camera and storage permission required]
         * Gallery [storage permission required]*/

    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

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

    private boolean checkCameraPermission() {
        //check if storage permission is enabled
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    @SuppressLint("NewApi")
    private void requestCameraPermission() {
        //request runtime storage permission
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when the user press Allow or Deny from permission request dialog
         * Im handling permission cases (allowed or denied)*/

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // picking from, camera first check if camera and storage are allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //Permission enabled
                        pickFromCamera();
                    } else {
                        //permission denied
                        Toast.makeText(this, "Please enable camera && storage permission ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {

                // picking from, gallery first check if storage are allowed or not
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
                break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //This method will be called after picking image form camera or gallery
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                // image is picked from gallery, get uri of image
                image_uri = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                    p_image.setText("Image picked from gallery");

                    ByteArrayOutputStream byteArrayOutputStreamObject ;

                    byteArrayOutputStreamObject = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

                    string_image = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                // image is picked from Camera, get uri of image
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                    p_image.setText("Image picked from camera");

                    ByteArrayOutputStream byteArrayOutputStreamObject ;

                    byteArrayOutputStreamObject = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

                    string_image = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v==p_image){
            pickImage();
        }
        if (v==submit){
            if (validate()){
                productsAPI.addItem();
                loader.showDialogue();
            }
        }
    }

    @Override
    public void onItemAdded(JSONObject object) throws JSONException {
        loader.hideDialogue();
        boolean success = object.getBoolean("success");
        String message = object.getString("message");

        if (success){
            Toasty.success(AddProductActivity.this, message, Toasty.LENGTH_LONG).show();
            startActivity(new Intent(new Intent(AddProductActivity.this, AdminHomeActivity.class)));
            finish();
        }
        else {
            Toasty.error(AddProductActivity.this, message, Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVolleyErrorResponse(VolleyError error) {
        loader.hideDialogue();
        if (error instanceof NetworkError){
            Toasty.error(this, "Check your connection and try again", Toasty.LENGTH_LONG).show();
        }
        else Toasty.error(this, error.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onJSONObjectException(JSONException e) {
        loader.hideDialogue();
        e.printStackTrace();
        Log.e("JSONException", e.toString());
    }

    @Override
    public void onProductsReceived(List<ModelProduct> productList) {

    }
}
