package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.data.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //views
    EditText firstnameEt, lastnameEt, emailEt, phoneEt, passwordEt, id_numberEt;
    RadioGroup radioGroup;
    RadioButton admin_radioButton,consultant_radioButton;
    Button registerBtn;

    private String user_role;

    private ProgressDialog loading;

    //register url
    private static String REGISTER_URL = "https://histogenetic-exhaus.000webhostapp.com/unishop/create_user.php"; // to include androids 9+
    //private static String REGISTER_URL = "http://110.110.11.96/unishop/create_user.php";  // my laptop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init views
        firstnameEt = findViewById(R.id.firstnameEt);
        lastnameEt = findViewById(R.id.lastnameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        passwordEt = findViewById(R.id.passwordEt);
        id_numberEt = findViewById(R.id.id_numberEt);
        radioGroup = findViewById(R.id.radioGroup);
        admin_radioButton = findViewById(R.id.admin_radioButton);
        consultant_radioButton = findViewById(R.id.consultant_radioButton);
        registerBtn = findViewById(R.id.registerBtn);

        loading = new ProgressDialog(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.admin_radioButton:
                        user_role = "admin";
                        consultant_radioButton.setChecked(false);
                        break;
                    case R.id.consultant_radioButton:
                        user_role = "consultant";
                        admin_radioButton.setChecked(false);
                        break;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = firstnameEt.getText().toString();
                String last_name = lastnameEt.getText().toString();
                String u_email = emailEt.getText().toString();
                String u_phone = phoneEt.getText().toString();
                String u_id_number = id_numberEt.getText().toString();
                String password = passwordEt.getText().toString();

                //Data validation
                if (first_name.length() != 0 && last_name.length() != 0 && u_email.length() != 0
                && u_phone.length() != 0 && u_id_number.length() != 0 && password.length() != 0
                        && user_role.length() != 0 && admin_radioButton.isChecked() || consultant_radioButton.isChecked()){

                    if (!Patterns.EMAIL_ADDRESS.matcher(u_email).matches()){
                        // Invalid email pattern set error
                        emailEt.setError("Invalid Email");
                        emailEt.setFocusable(true);
                    }
                    else {
                       // register user
                       registerNewUser(first_name,last_name,u_email,u_phone,u_id_number,password,user_role);
                    }

                }
                else {
                    //AlertDialog
                    AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                    //set Layout Linear Layout
                    LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
                    // Views to set in dialog
                    final TextView textView = new TextView(RegisterActivity.this);
                    textView.setText("Please fill all fields");
                    textView.setTextSize(20);
                    linearLayout.addView(textView);
                    linearLayout.setPadding(10,10,10,10);
                    builder.setView(linearLayout);


                    //cancel button
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss dialog
                            dialog.dismiss();
                        }
                    });
                    //create and show dialog
                    builder.create().show();
                }
            }
        });

    }

    private void registerNewUser(final String first_name, final String last_name, final String u_email,
                                 final String u_phone, final String u_id_number, final String password, final String user_role) {
        loading.setMessage("Registering...");
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success){
                                //AlertDialog
                                AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                                //set Layout Linear Layout
                                LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
                                // Views to set in dialog
                                final TextView textView = new TextView(RegisterActivity.this);
                                textView.setText(message);
                                textView.setTextSize(20);
                                linearLayout.addView(textView);
                                linearLayout.setPadding(10,10,10,10);
                                builder.setView(linearLayout);
                                //cancel button
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss dialog
                                        dialog.dismiss();
                                        firstnameEt.setText("");
                                        lastnameEt.setText("");
                                        emailEt.setText("");
                                        phoneEt.setText("");
                                        passwordEt.setText("");
                                        id_numberEt.setText("");
                                        radioGroup.clearCheck();
                                    }
                                });
                                //create and show dialog
                                builder.create().show();
                            }
                            else {
                                //AlertDialog
                                AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                                //set Layout Linear Layout
                                LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
                                // Views to set in dialog
                                final TextView textView = new TextView(RegisterActivity.this);
                                textView.setText(message);
                                textView.setTextSize(20);
                                linearLayout.addView(textView);
                                linearLayout.setPadding(10,10,10,10);
                                builder.setView(linearLayout);
                                //cancel button
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss dialog
                                        dialog.dismiss();
                                    }
                                });
                                //create and show dialog
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            loading.dismiss();
                            e.printStackTrace();
                            //AlertDialog
                            AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                            //set Layout Linear Layout
                            LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
                            // Views to set in dialog
                            final TextView textView = new TextView(RegisterActivity.this);
                            textView.setText(""+e.getMessage());
                            textView.setTextSize(20);
                            linearLayout.addView(textView);
                            linearLayout.setPadding(10,10,10,10);
                            builder.setView(linearLayout);
                            //cancel button
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss dialog
                                    dialog.dismiss();
                                }
                            });
                            //create and show dialog
                            builder.create().show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //AlertDialog
                        AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                        //set Layout Linear Layout
                        LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
                        // Views to set in dialog
                        final TextView textView = new TextView(RegisterActivity.this);
                        textView.setText(error.getMessage());
                        textView.setTextSize(20);
                        linearLayout.addView(textView);
                        linearLayout.setPadding(10,10,10,10);
                        builder.setView(linearLayout);
                        //cancel button
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss dialog
                                dialog.dismiss();
                            }
                        });
                        //create and show dialog
                        builder.create().show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", first_name);
                params.put("lastname", last_name);
                params.put("id_number", u_id_number);
                params.put("email", u_email);
                params.put("password", password);
                params.put("phone", u_phone);
                params.put("role", user_role);
                params.put("user_id", SharedHelper.getKey(RegisterActivity.this,"user_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
