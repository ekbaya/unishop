package consultant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.unishop.R;
import com.example.unishop.adapters.CategoriesAdapter;
import com.example.unishop.adapters.OrdersAdapter;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.api.OrderAPI;
import com.example.unishop.models.Product;
import com.example.unishop.services.OrderListener;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CheckoutActivity extends AppCompatActivity implements OrderListener.LoadOrdersListener {
    @BindView(R.id.cart_recyclerview) RecyclerView cart_recyclerview;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private OrdersAdapter ordersAdapter;
    private OrderAPI orderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        orderAPI = new OrderAPI(this);

        //setting its properties
        cart_recyclerview.setHasFixedSize(true);
        cart_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        orderAPI.setLoadOrdersListener(this);
        loadItemsInCart();
    }

    private void loadItemsInCart() {
        orderAPI.getOrders();
    }

    @Override
    public void onOrdersReceived(List<Product> orderList) {
        //initialise adapter
        ordersAdapter = new OrdersAdapter(this, orderList);
        //set adapter to recyclerView
        cart_recyclerview.setAdapter(ordersAdapter);
        progressBar.setVisibility(View.GONE);
        cart_recyclerview.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        Toasty.error(this,""+error.getMessage(), Toasty.LENGTH_LONG).show();
    }
}