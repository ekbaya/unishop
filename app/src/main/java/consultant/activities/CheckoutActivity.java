package consultant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.unishop.R;
import com.example.unishop.adapters.CategoriesAdapter;
import com.example.unishop.adapters.OrdersAdapter;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.api.OrderAPI;
import com.example.unishop.api.SalesAPI;
import com.example.unishop.models.Product;
import com.example.unishop.services.OrderListener;
import com.example.unishop.services.SalesListener;
import com.example.unishop.utilities.SharedHelper;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CheckoutActivity extends AppCompatActivity implements OrderListener.LoadOrdersListener ,
        View.OnClickListener, SalesListener.SaleOrderListener , OrderListener.RemoveItemsListener{
    @BindView(R.id.cart_recyclerview) RecyclerView cart_recyclerview;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.sendBtn) Button sendBtn;
    @BindView(R.id.totalTV) TextView totalTV;
    @BindView(R.id.balance_layout) LinearLayout balance_layout;
    @BindView(R.id.view) View view;
    private OrdersAdapter ordersAdapter;
    private OrderAPI orderAPI;
    private SalesAPI salesAPI;
    private List<Product> productList ;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        sendBtn.setOnClickListener(this);
        orderAPI = new OrderAPI(this);
        salesAPI = new SalesAPI(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Wait as we are saving your sale...");

        //setting its properties
        cart_recyclerview.setHasFixedSize(true);
        cart_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        orderAPI.setLoadOrdersListener(this);
        salesAPI.setSaleOrderListener(this);
        orderAPI.setRemoveItemsListener(this);
        productList = new ArrayList<>();
        loadItemsInCart();
    }

    private void loadItemsInCart() {
        orderAPI.getOrders();
    }

    @Override
    public void onOrdersReceived(List<Product> orderList) {
        if (orderList.size() != 0){
            productList = orderList;
            int total = 0;
            for (int i = 0; i < orderList.size(); i++){
                total = total + (Integer.parseInt(orderList.get(i).getPrice()) * Integer.parseInt("1"));
            }
            totalTV.setText("KES "+String.valueOf(total));
            //initialise adapter
            ordersAdapter = new OrdersAdapter(this, orderList);
            //set adapter to recyclerView
            cart_recyclerview.setAdapter(ordersAdapter);
            progressBar.setVisibility(View.GONE);
            balance_layout.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            cart_recyclerview.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            Toasty.warning(this, "No items added to Cart", Toasty.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        Toasty.error(this,""+error.getMessage(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        salesAPI.sellOrders(productList);
        dialog.show();

    }

    @Override
    public void onOrderPlaced() {
        orderAPI.removeItemsInCart();
    }

    @Override
    public void onItemsRemoved() {
        dialog.dismiss();
        Toasty.success(this, "Order saved successfully", Toasty.LENGTH_LONG).show();
        startActivity(new Intent(CheckoutActivity.this, DasboardActivity.class));
        finish();
    }

    @Override
    public void onFailureResponse(Exception e) {
        dialog.dismiss();
        Toasty.error(this, "Error saving your sales, "+e.getMessage(), Toasty.LENGTH_LONG).show();

    }
}