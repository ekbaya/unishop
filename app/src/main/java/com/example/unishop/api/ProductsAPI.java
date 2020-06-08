package com.example.unishop.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.R;
import com.example.unishop.data.SharedHelper;
import com.example.unishop.services.ProductsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductsAPI {
   private Context context;
   private ProductsListener productsListener;
   private ProgressDialog loading;

    public ProductsAPI(Context context) {
        this.context = context;
        loading = new ProgressDialog(context);
    }

    public void addItem(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.ADD_ITEM_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    productsListener.onSuccessResponse(jsonObject);
                } catch (JSONException e) {
                    productsListener.onJSONObjectException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productsListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("p_name", SharedHelper.getKey(context, "p_name"));
                params.put("p_category", SharedHelper.getKey(context, "category"));
                params.put("price", SharedHelper.getKey(context, "price"));
                params.put("quantity", SharedHelper.getKey(context, "quantity"));
                params.put("p_description", SharedHelper.getKey(context, "description"));
                params.put("image", SharedHelper.getKey(context, "image"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void showDialogue(){
        loading.setMessage("Wait a moment...");
        loading.show();
    }

    public void hideDialogue(){
        loading.dismiss();
    }


    public ProductsListener getProductsListener() {
        return productsListener;
    }

    public void setProductsListener(ProductsListener productsListener) {
        this.productsListener = productsListener;
    }
}
