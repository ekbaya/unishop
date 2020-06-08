package com.example.unishop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.R;
import com.example.unishop.utilities.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePhoneActivity extends AppCompatActivity {
    private EditText emailEt, phoneEt;
    private Button updateBtn;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        updateBtn = findViewById(R.id.updateBtn);

        final NetworkConnection connection = new NetworkConnection(this);
        loading = new ProgressDialog(this);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    String email = emailEt.getText().toString();
                    String phone = phoneEt.getText().toString();

                    if (connection.isConnected()){
                        updatePhone(email, phone);
                    }
                    else {
                        Toast.makeText(UpdatePhoneActivity.this, getText(R.string.network_text), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updatePhone(final String email, final String phone) {
        loading.setMessage("Please wait...");
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.UPDATE_PHONE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.i("onResponse", "["+response+"]");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success){
                                Toast.makeText(UpdatePhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                                emailEt.setText("");
                                phoneEt.setText("");
                            }
                            else {
                                Toast.makeText(UpdatePhoneActivity.this, message+" , check the email and try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            loading.dismiss();
                            e.printStackTrace();
                            Log.e("JSONException:", e.toString());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.e("VolleyError:", error.toString());

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePhoneActivity.this);
        requestQueue.add(stringRequest);


    }

    private boolean validate() {
        if (TextUtils.isEmpty(emailEt.getText().toString())) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            emailEt.setFocusable(true);
            return false;
        }
        else if (TextUtils.isEmpty(phoneEt.getText().toString())) {
            Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show();
            phoneEt.setFocusable(true);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            emailEt.getText().toString();
            return false;
        }
        else return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdatePhoneActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
