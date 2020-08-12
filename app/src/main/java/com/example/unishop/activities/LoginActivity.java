package com.example.unishop.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.models.User;
import com.example.unishop.utilities.SharedHelper;
import com.example.unishop.services.AccountListener;
import com.example.unishop.utilities.Loader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import consultant.DasboardActivity;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        AccountListener.LoginListener, AccountListener.AccountInstantListener , AccountListener.AccountRecoveryListener {
    @BindView(R.id.emailET) EditText emailET;
    @BindView(R.id.passwordET) EditText passwordET;
    @BindView(R.id.forgetPasswordTV) TextView forgetPasswordTV;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindView(R.id.btnRegister) Button btnRegister;

    private AccountAPI accountAPI;
    private Loader loader;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loginBtn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        forgetPasswordTV.setOnClickListener(this);
        accountAPI = new AccountAPI(this);
        accountAPI.setLoginListener(this);
        accountAPI.setAccountInstantListener(this);
        accountAPI.setAccountRecoveryListener(this);
        loader = new Loader(this);

    }

    private void checkUser() {
        if (SharedHelper.getBoolKey(LoginActivity.this, "loggedIn", false)){
            String role = SharedHelper.getKey(LoginActivity.this, "role");
            if (role.equals("admin")){
                startActivity(new Intent(new Intent(LoginActivity.this, AdminHomeActivity.class)));
                finish();
            }
            if (role.equals("consultant")){
                startActivity(new Intent(new Intent(LoginActivity.this, DasboardActivity.class)));
                finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
       if (v.equals(loginBtn)){
           //get data
           // input data
           String email = emailET.getText().toString();
           String password = passwordET.getText().toString().trim();

           if (validate()){
               accountAPI.loginUser(email, password);
               loader.showDialogue();
           }
       }
       if (v.equals(btnRegister)){
           startActivity(new Intent(new Intent(LoginActivity.this, RegisterActivity.class)));
       }
       if (v.equals(forgetPasswordTV)){
           showRecoveryPasswordDialog();
       }
    }

    private void showRecoveryPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        // Views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        /*sets the main width of EditView to fit a text of n 'M' letters regardless of the actual
        text extension and text size*/
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = emailEt.getText().toString().trim();
                accountAPI.recoverAccount(email);
                loader.showDialogue();
            }
        });
        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
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
    public void onSuccessLogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String email = user.getEmail();
            accountAPI.getUserAccount(email);
            loader.showDialogue();
        }

    }

    @Override
    public void onLoginFailure() {
        loader.hideDialogue();
        Toasty.error(LoginActivity.this, "Incorrect login credentials", Toasty.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailSent() {
        loader.hideDialogue();
        Toasty.success(LoginActivity.this, "Email sent check to reset password", Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onFailureSendingEmail() {
        loader.hideDialogue();
        Toasty.error(LoginActivity.this, "Operation failed , try again", Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onFailureResponse(Exception e) {
        loader.hideDialogue();
        Toasty.error(LoginActivity.this, "Failed... "+e.getMessage(), Toasty.LENGTH_SHORT).show();

    }

    @Override
    public void onAccountReceived(User user) {
        loader.hideDialogue();
        Toasty.success(LoginActivity.this, "Logged in successfully...", Toasty.LENGTH_SHORT).show();
        SharedHelper.putKey(LoginActivity.this, "role", user.getRole());
        SharedHelper.putKey(LoginActivity.this, "loggedIn", true);

        if (user.getRole().equals("admin")){
            startActivity(new Intent(new Intent(LoginActivity.this, AdminHomeActivity.class)));
            finish();
        }
        if (user.getRole().equals("consultant")){
            startActivity(new Intent(new Intent(LoginActivity.this, DasboardActivity.class)));
            finish();
        }
    }

    @Override
    public void onRequestCancelled(DatabaseError error) {
        loader.hideDialogue();
        Toasty.error(LoginActivity.this, error.getMessage(), Toasty.LENGTH_LONG).show();
    }
}
