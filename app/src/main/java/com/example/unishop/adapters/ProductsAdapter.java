package com.example.unishop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unishop.R;
import com.example.unishop.models.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends BaseAdapter {
    private Context context;
    List<ModelProduct> productList;

    public ProductsAdapter(Context context, List<ModelProduct> productList) {
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
            ImageView productImage = view.findViewById(R.id.imgProduct);
            TextView productDescription = view.findViewById(R.id.txtProductDescription);
            TextView productPrice = view.findViewById(R.id.txtPrice);
            TextView productCategory = view.findViewById(R.id.txtCategory);


            //set data
            productDescription.setText(productList.get(position).getName()+" - "+productList.get(position).getDescription());
            productPrice.setText("KSH "+productList.get(position).getPrice());
            productCategory.setText("Category: "+productList.get(position).getCategory());
            try {
                Picasso.get().load(productList.get(position).getImage_url()).into(productImage);

            }catch (Exception e){

            }
        }
        else {
            view = convertView;
        }
        return view;
    }
}
