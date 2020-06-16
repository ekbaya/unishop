package com.example.unishop.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.ConsultantsAPI;
import com.example.unishop.services.ConsultantListener;
import com.example.unishop.utilities.NetworkConnection;
import com.example.unishop.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ConsultantDetailsActivity extends AppCompatActivity implements ConsultantListener{
    @BindView(R.id.nameTv) TextView nameTv;
    @BindView(R.id.phoneTv) TextView phoneTv;
    @BindView(R.id.referred_byTv) TextView referred_byTv;
    @BindView(R.id.national_idTv) TextView national_idTv;
    @BindView(R.id.emailTv) TextView emailTv;
    @BindView(R.id.created_dateTv) TextView created_dateTv;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private Intent intent;
    private ConsultantsAPI consultantsAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        intent = getIntent();

        consultantsAPI = new ConsultantsAPI(this);
        consultantsAPI.setConsultantListener(this);
        //set data to views
        setDataToViews();
    }

    private void setDataToViews() {
        nameTv.setText(intent.getStringExtra("firstname")+" "+intent.getStringExtra("lastname"));
        phoneTv.setText(intent.getStringExtra("phone"));
        referred_byTv.setText(intent.getStringExtra("created_by"));
        national_idTv.setText(intent.getStringExtra("id_number"));
        emailTv.setText(intent.getStringExtra("email"));
        created_dateTv.setText(intent.getStringExtra("date_created"));

        String user_id = intent.getStringExtra("created_by");
        if (new NetworkConnection().get().isConnected(this)){
            consultantsAPI.getUserProfile(user_id);
        }else Toasty.warning(this, getText(R.string.network_text), Toasty.LENGTH_LONG).show();
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

    @Override
    public void onConsultantReceived(JSONObject object) throws JSONException {
        boolean success = object.getBoolean("success");
        String message = object.getString("message");
        JSONArray data = object.getJSONArray("data");

        if (success){
            JSONObject jsonObject = data.getJSONObject(0);
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");

            onUserReceived(firstname, lastname);
        }

    }

    @Override
    public void onVolleyErrorResponse(VolleyError error) {
        if (error instanceof NetworkError){
            Toasty.error(this, "Check your connection and try again", Toasty.LENGTH_LONG).show();
        }
        else Toasty.error(this, error.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onJSONObjectException(JSONException e) {
        e.printStackTrace();
        Log.e("JSONException", e.toString());
        progressBar.setVisibility(View.GONE);
        referred_byTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserReceived(String firstname, String lastname) {
        referred_byTv.setText(firstname+" "+lastname);
        progressBar.setVisibility(View.GONE);
        referred_byTv.setVisibility(View.VISIBLE);
    }
}
