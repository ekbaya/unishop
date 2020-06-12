package com.example.unishop.services;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface ConsultantListener {
    void onConsultantReceived(JSONObject object) throws JSONException;
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
    void onUserReceived(String firstname, String lastname);
}
