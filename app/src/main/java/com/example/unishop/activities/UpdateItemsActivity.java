package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class UpdateItemsActivity extends AppCompatActivity implements ProductsListener.UpdateQuantityListener, ProductsListener.UpdatePriceListener ,
        View.OnClickListener {
    @BindView(R.id.pp_editText) EditText pp_editText;
    @BindView(R.id.priceEt) EditText priceEt;
    @BindView(R.id.qp_editText) EditText qp_editText;
    @BindView(R.id.quantityEt) EditText quantityEt;
    @BindView(R.id.pupdateBtn) Button pupdateBtn;
    @BindView(R.id.qupdateBtn) Button qupdateBtn;

    private ProductsAPI productsAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);
        ButterKnife.bind(this);

        pupdateBtn.setOnClickListener(this);
        qupdateBtn.setOnClickListener(this);

        productsAPI = new ProductsAPI(this);
        productsAPI.setUpdatePriceListener(this);
        productsAPI.setUpdateQuantityListener(this);
        loader = new Loader(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateItemsActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        if (v.equals(pupdateBtn)){
            if (validatePrice()){
                String id = pp_editText.getText().toString();
                String price = priceEt.getText().toString();
                productsAPI.updatePrice(id, price);
                loader.showDialogue();
            }
        }
        if (v.equals(qupdateBtn)){
            if (validateQuantity()){
                String id = qp_editText.getText().toString();
                String quantity = quantityEt.getText().toString();
                productsAPI.updateQuantity(id, quantity);
                loader.showDialogue();
            }
        }
    }

    private boolean validateQuantity() {
        if (TextUtils.isEmpty(qp_editText.getText().toString())){
            Toasty.warning(this, "Invalid product id", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(quantityEt.getText().toString())){
            Toasty.warning(this, "Invalid Quantity", Toasty.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    private boolean validatePrice() {
        if (TextUtils.isEmpty(pp_editText.getText().toString())){
            Toasty.warning(this, "Invalid product id", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(priceEt.getText().toString())){
            Toasty.warning(this, "Invalid Price", Toasty.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    @Override
    public void onPriceUpdated() {
        loader.hideDialogue();
        Toasty.success(this, "Price updated successfully...", Toasty.LENGTH_LONG).show();
        qp_editText.setText("");
        quantityEt.setText("");

    }

    @Override
    public void onQuantityUpdated() {
        loader.hideDialogue();
        Toasty.success(this, "Quantity updated successfully...", Toasty.LENGTH_LONG).show();
        qp_editText.setText("");
        quantityEt.setText("");

    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {

    }
}
