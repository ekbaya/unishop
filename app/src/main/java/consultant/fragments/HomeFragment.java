package consultant.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.LoginActivity;
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
import consultant.activities.CheckoutActivity;

public class HomeFragment extends Fragment implements ProductsListener.LoadItemsListener {
    @BindView(R.id.gridView_layout) GridView productsGridView;
    private ProductsAPI productsAPI;
    private ProductsAdapter productsAdapter;
    private Loader loader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        productsAPI = new ProductsAPI(getActivity());
        productsAPI.setLoadItemsListener(this);
        loader = new Loader(getActivity());
        loadProducts();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu options in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);

        //hide add product
        MenuItem add_product = menu.findItem(R.id.action_add_product);
        add_product.setVisible(false);

        //hide add product
        MenuItem logout = menu.findItem(R.id.action_logout);
        logout.setShowAsAction(1);


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

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadProducts() {
        productsAPI.getAllProducts();
        loader.showDialogue();
    }

    private void searchProducts(String s) {
        productsAPI.searchProducts(s);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            showLogoutDialogue();
        }
        if (id == R.id.action_cart){
            startActivity(new Intent(getActivity(), CheckoutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialogue() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        // Views to set in dialog
        final TextView textView = new TextView(getActivity());
        textView.setText(getString(R.string.logout_warning));
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
                SharedHelper.clearSharedPreferences(getActivity());
                startActivity(new Intent(new Intent(getActivity(), LoginActivity.class)));
            }
        });

        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onProductsReceived(List<Product> productList) {
        loader.hideDialogue();
        productsAdapter = new ProductsAdapter(getActivity(), productList);
        productsGridView.setAdapter(productsAdapter);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        loader.hideDialogue();
    }
}
