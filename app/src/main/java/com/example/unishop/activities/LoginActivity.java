package com.example.unishop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.data.SharedHelper;
import com.example.unishop.services.AccountListiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import consultant.DasboardActivity;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        AccountListiner {
    @BindView(R.id.emailET) EditText emailET;
    @BindView(R.id.passwordET) EditText passwordET;
    @BindView(R.id.forgetPasswordTV) TextView forgetPasswordTV;
    @BindView(R.id.loginBtn) Button loginBtn;

    private AccountAPI accountAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        checkUser();
        loginBtn.setOnClickListener(this);
        accountAPI = new AccountAPI(this);
        accountAPI.setAccountListiner(this);

    }

    private void checkUser() {
        Log.d("Loggedin", String.valueOf(SharedHelper.getBoolKey(LoginActivity.this, "logged_in", false)));
        String role = String.valueOf(SharedHelper.getKey(LoginActivity.this, "role"));
        if (SharedHelper.getBoolKey(LoginActivity.this, "logged_in", false)){
            //check user role
            if (role.equals("admin")){
                //taking user to admin panel
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
            if (role.equals("consultant")){
                //taking user to admin panel
                Intent intent = new Intent(LoginActivity.this, DasboardActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
       if (v == loginBtn){
           //get data
           // input data
           String email = emailET.getText().toString();
           String password = passwordET.getText().toString().trim();

           if (validate()){
               accountAPI.loginUser(email, password);
               accountAPI.showDialogue();
           }
       }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(emailET.getText().toString())){
            Toasty.warning(this,"Invalid email", Toasty.LENGTH_LONG).show();
            emailET.setError("Invalid Email");
            emailET.setFocusable(true);
            return false;
        }
        else if (TextUtils.isEmpty(passwordET.getText().toString())){
            Toasty.warning(this,"Invalid password", Toasty.LENGTH_LONG).show();
            passwordET.setError("Invalid Email");
            passwordET.setFocusable(true);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()){
            Toasty.warning(this,"Invalid email", Toasty.LENGTH_LONG).show();
            emailET.setError("Invalid Email");
            emailET.setFocusable(true);
            return false;
        }
        else return true;
    }

    @Override
    public void onSuccessResponse(JSONObject object) throws JSONException {
        boolean success = object.getBoolean("success");
        String message = object.getString("message");
        JSONArray data = object.getJSONArray("data");

        if (success){
            accountAPI.hideDialogue();
            Toast.makeText(LoginActivity.this, "Login successfully...", Toast.LENGTH_SHORT).show();
            JSONObject dataJSONObject = data.getJSONObject(0);
            String user_id = dataJSONObject.getString("user_id");
            String email = dataJSONObject.getString("email");
            String phone = dataJSONObject.getString("phone");
            String firstname = dataJSONObject.getString("firstname");
            String lastname = dataJSONObject.getString("lastname");
            String role = dataJSONObject.getString("role");

            //save data to SharedPreference
            SharedHelper.putKey(LoginActivity.this,"user_id", user_id);
            SharedHelper.putKey(LoginActivity.this,"email", email);
            SharedHelper.putKey(LoginActivity.this,"phone", phone);
            SharedHelper.putKey(LoginActivity.this,"firstname", firstname);
            SharedHelper.putKey(LoginActivity.this,"lastname", lastname);
            SharedHelper.putKey(LoginActivity.this,"role", role);
            SharedHelper.putKey(LoginActivity.this, "logged_in", true);

            //check user role
            if (role.equals("admin")){
                //taking user to admin panel
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
            if (role.equals("consultant")){
                //taking user to admin panel
                Intent intent = new Intent(LoginActivity.this, DasboardActivity.class);
                startActivity(intent);
                finish();
            }

        }
        else {
            accountAPI.hideDialogue();
            Toasty.error(this, message, Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVolleyErrorResponse(VolleyError error) {
        accountAPI.hideDialogue();
        if (error instanceof NetworkError){
            Toasty.error(this, "Check your connection and try again", Toasty.LENGTH_LONG).show();
        }
        else Toasty.error(this, error.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onJSONObjectException(JSONException e) {
        accountAPI.hideDialogue();
        Toasty.error(this, e.toString(), Toasty.LENGTH_LONG).show();
    }
}
