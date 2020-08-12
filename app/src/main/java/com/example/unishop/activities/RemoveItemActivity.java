package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class RemoveItemActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btnRemove) Button btnRemove;
    @BindView(R.id.edtItemID) EditText edtItemID;

    private Loader loader;
    private ProductsAPI productsAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Remove Item");
        ButterKnife.bind(this);
        btnRemove.setOnClickListener(this);

        loader = new Loader(this);
        productsAPI = new ProductsAPI(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnRemove)){
            if (validate()){
                String id = edtItemID.getText().toString();
                //productsAPI.deleteProduct(id);
                loader.showDialogue();
            }
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(edtItemID.getText().toString())){
            Toasty.warning(this, "Invalid Item ID", Toasty.LENGTH_LONG).show();
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
        Intent intent = new Intent(RemoveItemActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
