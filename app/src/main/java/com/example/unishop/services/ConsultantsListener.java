package com.example.unishop.services;

import com.android.volley.VolleyError;
import com.example.unishop.models.ModelConsultant;

import org.json.JSONException;

import java.util.List;

public interface ConsultantsListener {
    void onConsultantsReceived(List<ModelConsultant> consultantArrayList);
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
}
