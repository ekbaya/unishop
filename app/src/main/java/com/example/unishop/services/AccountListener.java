package com.example.unishop.services;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface AccountListener {
    void onSuccessResponse(JSONObject object) throws JSONException;
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
}
