package com.example.unishop.services;

import com.android.volley.VolleyError;
import com.example.unishop.models.ModelProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface ProductsListener {
    void onItemAdded(JSONObject object) throws JSONException;
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
    void onProductsReceived(List<ModelProduct> productList);

    interface UpdateListener{
        void onPriceUpdated(JSONObject object) throws JSONException;
        void onQuantityUpdated(JSONObject object) throws JSONException;
        void onProductDeleted(JSONObject object) throws JSONException;
        void onVolleyErrorResponse(VolleyError error);
        void onJSONObjectException(JSONException e);
    }
}
