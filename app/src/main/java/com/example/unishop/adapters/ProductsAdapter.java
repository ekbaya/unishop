package com.example.unishop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.unishop.R;
import com.example.unishop.activities.UpdateProductActivity;
import com.example.unishop.api.OrderAPI;
import com.example.unishop.api.ProductsAPI;
import com.example.unishop.models.Product;
import com.example.unishop.services.OrderListener;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Loader;
import com.squareup.picasso.Picasso;

import java.util.List;

import consultant.activities.CheckoutActivity;
import es.dmoral.toasty.Toasty;

public class ProductsAdapter extends BaseAdapter implements ProductsListener.removeProductLister{
    private Context context;
    List<Product> productList;
    Loader loader;
    ProductsAPI productsAPI;
    OrderAPI orderAPI;

    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        loader = new Loader(context);
        productsAPI = new ProductsAPI(context);
        productsAPI.setRemoveProductLister(this);
        orderAPI = new OrderAPI(context);
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
                    else if (context.getClass().getSimpleName().equalsIgnoreCase("RemoveItemActivity")){
                        showDeleteProductDialogue(productList.get(position).getId(), productList.get(position).getImage());
                    }
                    else {
                        // This is the sales personnel view
                        showAddToCartDialogue(productList.get(position));
                    }
                }
            });
        }
        else {
            view = convertView;
        }
        return view;
    }

    private void showAddToCartDialogue(Product product) {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(context);
        // Views to set in dialog
        final TextView textView = new TextView(context);
        textView.setText(R.string.add_cart);
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
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                orderAPI.addProductToCart(product);
                loader.showDialogue();
                orderAPI.setAddToCartListener(new OrderListener.AddToCartListener() {
                    @Override
                    public void onItemAdded() {
                      loader.hideDialogue();
                      showCheckoutDialogue();
                    }

                    @Override
                    public void onFailureResponse(Exception e) {
                        loader.hideDialogue();
                        Toasty.error(context, "Error adding item to cart, "+e.getMessage(), Toasty.LENGTH_LONG).show();

                    }
                });

            }
        });

        //create and show dialog
        builder.create().show();
    }

    private void showCheckoutDialogue() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(context);
        // Views to set in dialog
        final TextView textView = new TextView(context);
        textView.setText(R.string.added_to_cart);
        textView.setTextSize(20);
        linearLayout.addView(textView);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        //cancel button
        builder.setNegativeButton("CONTINUE SHOPPING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //Reset pin button
        builder.setPositiveButton("PROCEED TO CHECKOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, CheckoutActivity.class));
            }
        });

        //create and show dialog
        builder.create().show();
    }

    private void showDeleteProductDialogue(String id, String image) {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(context);
        // Views to set in dialog
        final TextView textView = new TextView(context);
        textView.setText(R.string.detete_product_txt);
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
                //calling api
                productsAPI.deleteProduct(id, image);
                loader.showDialogue();
            }
        });

        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onProductRemoved() {
        loader.hideDialogue();
        Toasty.success(context, "Product deleted successfully", Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onFailureDeletingProductImage(Exception e) {
        loader.hideDialogue();
        Toasty.error(context, "Failed to delete product Image, "+e.getMessage(), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onFailureRemovingProduct(Exception e) {
        loader.hideDialogue();
        Toasty.error(context, "Failed to remove the product, "+e.getMessage(), Toasty.LENGTH_LONG).show();
    }
}
