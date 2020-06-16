package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.adapters.ProductsAdapter;
import com.example.unishop.models.ModelProduct;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ProductsActivity extends AppCompatActivity implements ProductsListener {
    @BindView(R.id.gridView_layout) GridView productsGridView;
    private ProductsAPI productsAPI;
    private ProductsAdapter productsAdapter;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All Products");
        ButterKnife.bind(this);

        productsAPI = new ProductsAPI(this);
        productsAPI.setProductsListener(this);
        productsAPI.getAllProducts();

        loader = new Loader(this);
        loader.showDialogue();
    }

    @Override
    public void onItemAdded(JSONObject object) throws JSONException {

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
    public void onProductsReceived(List<ModelProduct> productList) {
        loader.hideDialogue();
        productsAdapter = new ProductsAdapter(ProductsActivity.this, productList);
        productsGridView.setAdapter(productsAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductsActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
