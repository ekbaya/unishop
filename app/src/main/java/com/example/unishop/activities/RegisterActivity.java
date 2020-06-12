package com.example.unishop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.services.AccountListener;
import com.example.unishop.utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        AccountListener, RadioGroup.OnCheckedChangeListener {
    //views
    @BindView(R.id.firstnameEt) EditText firstnameEt;
    @BindView(R.id.lastnameEt) EditText lastnameEt;
    @BindView(R.id.emailEt) EditText emailEt;
    @BindView(R.id.phoneEt) EditText phoneEt;
    @BindView(R.id.passwordET) EditText passwordEt;
    @BindView(R.id.id_numberEt)EditText id_numberEt;
    @BindView(R.id.registerBtn) Button registerBtn;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.admin_radioButton) RadioButton admin_radioButton;
    @BindView(R.id.consultant_radioButton) RadioButton consultant_radioButton;


    private String user_role;
    private AccountAPI accountAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        registerBtn.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        accountAPI = new AccountAPI(this);
        accountAPI.setAccountListener(this);
        loader = new Loader(this);

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

    @Override
    public void onClick(View v) {
       if (v == registerBtn){
           String first_name = firstnameEt.getText().toString();
           String last_name = lastnameEt.getText().toString();
           String u_email = emailEt.getText().toString();
           String u_phone = phoneEt.getText().toString();
           String u_id_number = id_numberEt.getText().toString();
           String password = passwordEt.getText().toString();

           if (validate()){
               loader.showDialogue();
               accountAPI.registerNewUser(first_name,last_name,u_email,u_phone,u_id_number,password,user_role);
           }
       }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(firstnameEt.getText().toString())){
            Toasty.warning(this,"Invalid First name", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(lastnameEt.getText().toString())){
            Toasty.warning(this,"Invalid Last name", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(emailEt.getText().toString())){
            Toasty.warning(this,"Invalid Email", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(phoneEt.getText().toString())){
            Toasty.warning(this,"Invalid Phone", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(id_numberEt.getText().toString())){
            Toasty.warning(this,"Invalid ID Number", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(passwordEt.getText().toString())){
            Toasty.warning(this,"Invalid Password", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()){
            Toasty.warning(this,"Invalid Email", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (!admin_radioButton.isChecked() && !consultant_radioButton.isChecked()){
            Toasty.warning(this,"Invalid user role", Toasty.LENGTH_LONG).show();
             return false;
        }else return true;
    }

    @Override
    public void onSuccessResponse(JSONObject object) throws JSONException {
        loader.hideDialogue();
        boolean success = object.getBoolean("success");
        String message = object.getString("message");

        if (success){
            Toasty.success(this, message, Toasty.LENGTH_LONG).show();
        }
        else {
            Toasty.error(this, message, Toasty.LENGTH_LONG).show();
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
        Toasty.error(this, e.toString(), Toasty.LENGTH_LONG).show();
    }

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
}
