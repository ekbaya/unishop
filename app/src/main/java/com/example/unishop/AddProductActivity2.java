package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.data.SharedHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity2 extends AppCompatActivity {
    private EditText price, quantity, p_description;
    private TextView p_image;
    private Button submit;

    // Progress Dialog
    private ProgressDialog pDialog;


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

        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select an image
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
            SharedHelper.putKey(AddProductActivity2.this, "image", p_image.getText().toString());
            return true;
        }
    }

    private void addItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}
