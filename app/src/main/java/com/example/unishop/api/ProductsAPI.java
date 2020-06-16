package com.example.unishop.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.models.ModelProduct;
import com.example.unishop.services.ProductsListener;
import com.example.unishop.utilities.Server;
import com.example.unishop.utilities.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsAPI {
   private Context context;
   private ProductsListener productsListener;
   private ProductsListener.UpdateListener updateListener;
   List<ModelProduct> productList;

    public ProductsAPI(Context context) {
        this.context = context;
        productList = new ArrayList<>();
    }

    public void addItem(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.get().ADD_ITEM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    productsListener.onItemAdded(jsonObject);
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
                params.put("name", SharedHelper.getKey(context, "p_name"));
                params.put("category_id", SharedHelper.getKey(context, "category_id"));
                params.put("price", SharedHelper.getKey(context, "price"));
                params.put("quantity", SharedHelper.getKey(context, "quantity"));
                params.put("description", SharedHelper.getKey(context, "description"));
                params.put("image_url", SharedHelper.getKey(context, "image_url"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void updatePrice(String id, String price){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.get().UPDATE_PRICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    updateListener.onPriceUpdated(jsonObject);
                } catch (JSONException e) {
                    updateListener.onJSONObjectException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("price", price);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void updateQuantity(String id, String quantity){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.get().UPDATE_QUANTITY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    updateListener.onQuantityUpdated(jsonObject);
                } catch (JSONException e) {
                    updateListener.onJSONObjectException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("price", quantity);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void  getAllProducts(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.get().PRODUCTS_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("RESPONSE", response.toString());
                        int count = 0;
                        while (count<response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                ModelProduct modelProduct = new ModelProduct(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("category"),
                                        jsonObject.getString("price"),
                                        jsonObject.getString("quantity"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("image_url")
                                );
                                productList.add(modelProduct);
                                count++;

                            } catch (JSONException e) {
                                productsListener.onJSONObjectException(e);
                            }
                        }

                        productsListener.onProductsReceived(productList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productsListener.onVolleyErrorResponse(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public void deleteProduct(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, new Server().DELETE_PRODUCT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    updateListener.onProductDeleted(jsonObject);
                } catch (JSONException e) {
                    updateListener.onJSONObjectException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public ProductsListener getProductsListener() {
        return productsListener;
    }

    public void setProductsListener(ProductsListener productsListener) {
        this.productsListener = productsListener;
    }

    public ProductsListener.UpdateListener getUpdateListener() {
        return updateListener;
    }

    public void setUpdateListener(ProductsListener.UpdateListener updateListener) {
        this.updateListener = updateListener;
    }
}
