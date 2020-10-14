package com.example.unishop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unishop.R;
import com.example.unishop.models.Product;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyHolder>{
    private Context context;
    private List<Product> orders;

    public OrdersAdapter(Context context, List<Product> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_order.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false);
        return new OrdersAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        final String name = orders.get(position).getName();
        final String description = orders.get(position).getDescription();
        final String quantity = orders.get(position).getQuantity();

        //set Views
        holder.nameTv.setText(name);
        holder.quantityTv.setText(quantity);
        holder.descriptionTv.setText(description);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //views
        TextView nameTv, descriptionTv, quantityTv;
        Button subBtn, addBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            nameTv = itemView.findViewById(R.id.nameTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            subBtn = itemView.findViewById(R.id.subBtn);
            addBtn = itemView.findViewById(R.id.addBtn);

        }
    }
}
