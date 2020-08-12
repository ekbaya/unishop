package com.example.unishop.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.example.unishop.R;
import com.example.unishop.adapters.ProductsAdapter;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.models.Product;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;
import com.example.unishop.utilities.SharedHelper;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity implements ProductsListener.LoadItemsListener {
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
        productsAPI.setLoadItemsListener(this);
        loader = new Loader(this);
        loadProducts();
    }

    @Override
    public void onProductsReceived(List<Product> productList) {
        loader.hideDialogue();
        productsAdapter = new ProductsAdapter(ProductsActivity.this, productList);
        productsGridView.setAdapter(productsAdapter);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {

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

    private void loadProducts() {
        productsAPI.getAllProducts();
        loader.showDialogue();
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
        AlertDialog.Builder builder= new AlertDialog.Builder(ProductsActivity.this);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(ProductsActivity.this);
        // Views to set in dialog
        final TextView textView = new TextView(ProductsActivity.this);
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
                SharedHelper.clearSharedPreferences(ProductsActivity.this);
                startActivity(new Intent(new Intent(ProductsActivity.this, LoginActivity.class)));
                finish();
            }
        });

        //create and show dialog
        builder.create().show();
    }
}
