package com.example.unishop.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.models.ModelCategory;
import com.example.unishop.services.CategoriesListener;
import com.example.unishop.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAPI {
    private List<ModelCategory> categoryList;
    private Context context;
    private CategoriesListener categoriesListener;

    public CategoriesAPI(Context context) {
        this.context = context;
        categoryList = new ArrayList<>();
    }

    public void getCategories(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.get().CATEGORIES_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("RESPONSE", response.toString());

                        int count = 0;
                        while (count<response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                ModelCategory modelCategory = new ModelCategory(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("description")
                                );
                                categoryList.add(modelCategory);
                                count++;

                            } catch (JSONException e) {
                                categoriesListener.onJSONObjectException(e);
                            }
                        }

                        categoriesListener.onCategoriesReceived(categoryList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                categoriesListener.onVolleyErrorResponse(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public CategoriesListener getCategoriesListener() {
        return categoriesListener;
    }

    public void setCategoriesListener(CategoriesListener categoriesListener) {
        this.categoriesListener = categoriesListener;
    }
}
