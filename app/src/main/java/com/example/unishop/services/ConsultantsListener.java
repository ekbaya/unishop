package com.example.unishop.services;

import com.android.volley.VolleyError;
import com.example.unishop.data.models.ModelConsultant;

import org.json.JSONException;

import java.util.List;

public interface ConsultantsListener {
    void onSuccessResponse(List<ModelConsultant> consultantArrayList);
    void onVolleyErrorResponse(VolleyError error);
    void onJSONObjectException(JSONException e);
}
