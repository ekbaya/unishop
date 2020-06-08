package com.example.unishop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.R;
import com.example.unishop.data.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity2 extends AppCompatActivity {
    private EditText price, quantity, p_description;
    private TextView p_image;
    private Button submit;

    // Progress Dialog
    private ProgressDialog loading;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        p_description = findViewById(R.id.p_description);
        p_image = findViewById(R.id.p_image);
        submit = findViewById(R.id.submit);


        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        loading = new ProgressDialog(this);

        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pickImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    addItem();
                }
            }
        });
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
            SharedHelper.putKey(AddProductActivity2.this, "price", price.getText().toString());
            SharedHelper.putKey(AddProductActivity2.this, "quantity", quantity.getText().toString());
            SharedHelper.putKey(AddProductActivity2.this, "description", p_description.getText().toString());
            SharedHelper.putKey(AddProductActivity2.this, "image", string_image);
            return true;
        }
    }

    private void addItem() {
        loading.setMessage("Please wait...");
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.ADD_ITEM_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.i("JSONResponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success){
                        Toast.makeText(AddProductActivity2.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(new Intent(AddProductActivity2.this, AdminHomeActivity.class)));
                        finish();
                    }
                    else {
                        Toast.makeText(AddProductActivity2.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.e("VolleyError", error.toString());
                Toast.makeText(AddProductActivity2.this, "Ops! some error occurred try again ", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("p_name", SharedHelper.getKey(AddProductActivity2.this, "p_name"));
                params.put("p_category", SharedHelper.getKey(AddProductActivity2.this, "category"));
                params.put("price", SharedHelper.getKey(AddProductActivity2.this, "price"));
                params.put("quantity", SharedHelper.getKey(AddProductActivity2.this, "quantity"));
                params.put("p_description", SharedHelper.getKey(AddProductActivity2.this, "description"));
                params.put("image", SharedHelper.getKey(AddProductActivity2.this, "image"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddProductActivity2.this);
        requestQueue.add(stringRequest);
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
        Intent intent = new Intent(AddProductActivity2.this, AdminHomeActivity.class);
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

}
