package com.example.unishop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class UpdatePhoneActivity extends AppCompatActivity implements View.OnClickListener, UpdatePhoneListener {
    @BindView(R.id.emailEt) EditText emailEt;
    @BindView(R.id.phoneEt) EditText phoneEt;
    @BindView(R.id.updateBtn) Button updateBtn;

    private ConsultantsAPI consultantsAPI;
    private Loader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);
        updateBtn.setOnClickListener(this);

        consultantsAPI = new ConsultantsAPI(this);
        consultantsAPI.setUpdatePhoneListener(this);
        loader = new Loader(this);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(emailEt.getText().toString())) {
            Toasty.warning(this, "Invalid Email", Toasty.LENGTH_LONG).show();
            emailEt.setFocusable(true);
            return false;
        }
        else if (TextUtils.isEmpty(phoneEt.getText().toString())) {
            Toasty.warning(this, "Invalid Phone", Toasty.LENGTH_LONG).show();
            phoneEt.setFocusable(true);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()){
            Toasty.warning(this, "Invalid Email", Toasty.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
       if (v == updateBtn){
           if (validate()){
               String email = emailEt.getText().toString();
               String phone = phoneEt.getText().toString();

               consultantsAPI.updatePhone(email, phone);
               loader.showDialogue();
           }
       }
    }

    @Override
    public void onPhoneUpdated(JSONObject object) throws JSONException {
        loader.hideDialogue();
        boolean success = object.getBoolean("success");
        String message = object.getString("message");

        if (success){
            Toasty.success(UpdatePhoneActivity.this, message, Toasty.LENGTH_LONG).show();
            emailEt.setText("");
            phoneEt.setText("");
        }
        else {
            Toasty.error(UpdatePhoneActivity.this, message + " , check the email and try again", Toasty.LENGTH_LONG).show();
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
}
