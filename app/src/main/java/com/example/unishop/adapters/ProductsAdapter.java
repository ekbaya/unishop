package com.example.unishop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.unishop.R;
import com.example.unishop.activities.UpdateProductActivity;
import com.example.unishop.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductsAdapter extends BaseAdapter {
    private Context context;
    List<Product> productList;

    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.row_product,null);
            CardView productCard = view.findViewById(R.id.productCard);
            ImageView productImage = view.findViewById(R.id.imgProduct);
            TextView productDescription = view.findViewById(R.id.txtProductDescription);
            TextView productPrice = view.findViewById(R.id.txtPrice);
            TextView productCategory = view.findViewById(R.id.txtCategory);


            //set data
            productDescription.setText(productList.get(position).getName()+" - "+productList.get(position).getDescription());
            productPrice.setText("KSH "+productList.get(position).getPrice());
            productCategory.setText("Category: "+productList.get(position).getCategory());
            try {
                Picasso.get().load(productList.get(position).getImage()).into(productImage);

            }catch (Exception e){

            }

            productCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context.getClass().getSimpleName().equalsIgnoreCase("UpdateItemsActivity")){
                        String id = productList.get(position).getId();
                        String name = productList.get(position).getName();
                        String price = productList.get(position).getPrice();
                        String quantity = productList.get(position).getQuantity();
                        String description = productList.get(position).getDescription();
                        String category = productList.get(position).getCategory();
                        String image = productList.get(position).getImage();
                        Intent intent = new Intent(new Intent(context, UpdateProductActivity.class));
                        intent.putExtra("productID", id);
                        intent.putExtra("name", name);
                        intent.putExtra("price", price);
                        intent.putExtra("quantity", quantity);
                        intent.putExtra("description", description);
                        intent.putExtra("category", category);
                        intent.putExtra("image", image);
                        context.startActivity(intent);
                    }
                }
            });
        }
        else {
            view = convertView;
        }
        return view;
    }
}
