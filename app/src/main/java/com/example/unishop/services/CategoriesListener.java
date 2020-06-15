package com.example.unishop.services;

import com.android.volley.VolleyError;
import com.example.unishop.models.ModelCategory;

import org.json.JSONException;

import java.util.List;

public interface CategoriesListener {
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
    void onCategoriesReceived(List<ModelCategory> categoryList);
}
