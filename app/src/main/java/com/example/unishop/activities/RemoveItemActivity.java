package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.adapters.ProductsAdapter;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.models.Product;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;
import com.example.unishop.utilities.SharedHelper;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class RemoveItemActivity extends AppCompatActivity implements ProductsListener.LoadItemsListener{
    @BindView(R.id.gridView_layout) GridView productsGridView;
    private ProductsAPI productsAPI;
    private ProductsAdapter productsAdapter;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Remove Item");
        ButterKnife.bind(this);

        productsAPI = new ProductsAPI(this);
        productsAPI.setLoadItemsListener(this);
        loader = new Loader(this);
        loadProducts();
    }

    private void loadProducts() {
        productsAPI.getAllProducts();
        loader.showDialogue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //searchView to search product by product name/description
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button
                if (!TextUtils.isEmpty(s)){
                    searchProducts(s);
                }
                else {
                    loadProducts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called as and when user press any letter
                if (!TextUtils.isEmpty(s)){
                    searchProducts(s);
                }
                else {
                    loadProducts();
                }
                return false;
            }
        });
        return true;
    }

    private void searchProducts(String s) {
        productsAPI.searchProducts(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            showLogoutDialogue();
        }
        if (id == R.id.action_add_product){
            startActivity(new Intent(this, CategoriesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialogue() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(RemoveItemActivity.this);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(RemoveItemActivity.this);
        // Views to set in dialog
        final TextView textView = new TextView(RemoveItemActivity.this);
        textView.setText("Are you sure you want to exit?");
        textView.setTextSize(20);
        linearLayout.addView(textView);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        //cancel button
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //Reset pin button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedHelper.clearSharedPreferences(RemoveItemActivity.this);
                startActivity(new Intent(new Intent(RemoveItemActivity.this, LoginActivity.class)));
                finish();
            }
        });

        //create and show dialog
        builder.create().show();
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

    @Override
    public void onProductsReceived(List<Product> productList) {
        loader.hideDialogue();
        productsAdapter = new ProductsAdapter(RemoveItemActivity.this, productList);
        productsGridView.setAdapter(productsAdapter);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {

    }
}
