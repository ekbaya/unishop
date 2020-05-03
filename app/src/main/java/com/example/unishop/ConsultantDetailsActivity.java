package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConsultantDetailsActivity extends AppCompatActivity {
    private TextView nameTv, phoneTv, referred_byTv, national_idTv, emailTv, created_dateTv;
    private Intent intent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init views
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        referred_byTv = findViewById(R.id.referred_byTv);
        national_idTv = findViewById(R.id.national_idTv);
        emailTv = findViewById(R.id.emailTv);
        created_dateTv = findViewById(R.id.created_dateTv);
        progressBar = findViewById(R.id.progressBar);

        NetworkConnection networkConnection = new NetworkConnection(this);

        intent = getIntent();

        //set data to views
        nameTv.setText(intent.getStringExtra("firstname")+" "+intent.getStringExtra("lastname"));
        phoneTv.setText(intent.getStringExtra("phone"));
        referred_byTv.setText(intent.getStringExtra("created_by"));
        national_idTv.setText(intent.getStringExtra("id_number"));
        emailTv.setText(intent.getStringExtra("email"));
        created_dateTv.setText(intent.getStringExtra("date_created"));

        String user_id = intent.getStringExtra("created_by");

        if (networkConnection.isConnected()){
            getUserProfile(user_id);
        }else Toast.makeText(this, getText(R.string.network_text), Toast.LENGTH_SHORT).show();
    }

    private void getUserProfile(final String user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.USER_PROFILE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            JSONArray data = jsonObject.getJSONArray("data");

                            if (success){
                                JSONObject object = data.getJSONObject(0);
                                String firstname = object.getString("firstname");
                                String lastname = object.getString("lastname");

                                setNameToTextView(firstname, lastname);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", e.toString());
                            progressBar.setVisibility(View.GONE);
                            referred_byTv.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                progressBar.setVisibility(View.GONE);
                referred_byTv.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", user_id);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setNameToTextView(String firstname, String lastname) {
        referred_byTv.setText(firstname+" "+lastname);
        progressBar.setVisibility(View.GONE);
        referred_byTv.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConsultantDetailsActivity.this, ConsultantsActivity.class);
        startActivity(intent);
        finish();
    }
}
