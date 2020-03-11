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

public class LoginActivity extends AppCompatActivity {
    EditText emailET,passwordET;
    TextView forgetPasswordTV;
    private Button loginBtn;

    private ProgressDialog loading;

    //login url
    //private static String LOGIN_URL = "http://histogenetic-exhaus.000webhostapp.com/unishop/authenticate.php";
    private static String LOGIN_URL = "http://110.110.11.96/unishop/authenticate.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        forgetPasswordTV = findViewById(R.id.forgetPasswordTV);
        loginBtn = findViewById(R.id.loginBtn);

        loading = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data
                // input data
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        // Invalid email pattern set error
                        emailET.setError("Invalid Email");
                        emailET.setFocusable(true);
                    }
                    else {
                        // valid email pattern
                        loginUser(email,password);
                    }
                }
                else {
                    //AlertDialog
                    AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Warning!!!");
                    //set Layout Linear Layout
                    LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
                    // Views to set in dialog
                    final TextView textView = new TextView(LoginActivity.this);
                    textView.setText("Please fill all the details");
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

    private void loginUser(final String email, final String password) {
        loading.setMessage("Signing in...");
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
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
                                loading.dismiss();
                                Toast.makeText(LoginActivity.this, "Login successfully...", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < data.length(); i++){
                                    JSONObject object = data.getJSONObject(i);
                                    String user_id = object.getString("user_id");
                                    String email = object.getString("email");
                                    String phone = object.getString("phone");
                                    String firstname = object.getString("firstname");
                                    String lastname = object.getString("lastname");
                                    String role = object.getString("role");

                                    //check user role
                                    if (role.equals("admin")){
                                        //taking user to admin panel
                                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        intent.putExtra("email",email);
                                        intent.putExtra("phone",phone);
                                        intent.putExtra("firstname",firstname);
                                        intent.putExtra("lastname",lastname);
                                        startActivity(intent);
                                        finish();
                                    }


                                }

                            }
                            else {
                                loading.dismiss();
                                //AlertDialog
                                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Error!!!");
                                //set Layout Linear Layout
                                LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
                                // Views to set in dialog
                                final TextView textView = new TextView(LoginActivity.this);
                                textView.setText(message);
                                textView.setTextSize(20);
                                linearLayout.addView(textView);
                                linearLayout.setPadding(10,10,10,10);
                                builder.setView(linearLayout);


                                //cancel button
                                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss dialog
                                        dialog.dismiss();
                                    }
                                });

                                //Reset pin button
                                builder.setPositiveButton("Reset Pin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //showResetPinDialogue();
                                    }
                                });

                                //create and show dialog
                                builder.create().show();
                            }



                        }catch (JSONException e){
                            loading.dismiss();
                            //AlertDialog
                            AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Error!!!");
                            //set Layout Linear Layout
                            LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
                            // Views to set in dialog
                            final TextView textView = new TextView(LoginActivity.this);
                            textView.setText("..."+e.getMessage());
                            textView.setTextSize(20);
                            linearLayout.addView(textView);
                            linearLayout.setPadding(10,10,10,10);
                            builder.setView(linearLayout);

                            //cancel button
                            builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss dialog
                                    dialog.dismiss();
                                }
                            });

                            //Reset pin button
                            builder.setPositiveButton("Reset Pin", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //showResetPinDialogue();
                                }
                            });

                            //create and show dialog
                            builder.create().show();


                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //AlertDialog
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error!!!");
                //set Layout Linear Layout
                LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
                // Views to set in dialog
                final TextView textView = new TextView(LoginActivity.this);
                textView.setText("Ops! some error occurred check and try again");
                textView.setTextSize(20);
                linearLayout.addView(textView);
                linearLayout.setPadding(10,10,10,10);
                builder.setView(linearLayout);

                //cancel button
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });

                //Reset pin button
                builder.setPositiveButton("Reset Pin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // showResetPinDialogue();
                    }
                });

                //create and show dialog
                builder.create().show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
