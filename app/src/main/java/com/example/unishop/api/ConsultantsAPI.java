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
import com.example.unishop.R;
import com.example.unishop.models.ModelConsultant;
import com.example.unishop.services.ConsultantListener;
import com.example.unishop.services.ConsultantsListener;
import com.example.unishop.services.UpdatePhoneListener;
import com.example.unishop.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultantsAPI {
    private List<ModelConsultant> consultantArrayList;
    private Context context;
    private ConsultantsListener consultantsListener;
    private ConsultantListener consultantListener;
    private UpdatePhoneListener updatePhoneListener;

    public ConsultantsAPI(Context context) {
        this.context = context;
        consultantArrayList = new ArrayList<>();
    }

    public void getAllConsultants() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.get().CONSULTANT_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("RESPONSE", response.toString());
                        int count = 0;
                        while (count<response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                ModelConsultant modelConsultant = new ModelConsultant(
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("firstname"),
                                        jsonObject.getString("lastname"),
                                        jsonObject.getString("id_number"),
                                        jsonObject.getString("phone"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("reg_date"),
                                        jsonObject.getString("refferred_by")
                                );
                                consultantArrayList.add(modelConsultant);
                                count++;

                            } catch (JSONException e) {
                                consultantsListener.onJSONObjectException(e);
                            }
                        }

                        consultantsListener.onConsultantsReceived(consultantArrayList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                consultantsListener.onVolleyErrorResponse(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public void getUserProfile(final String user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.get().USER_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            consultantListener.onConsultantReceived(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", e.toString());
                            consultantListener.onJSONObjectException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                consultantListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", user_id);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void updatePhone(final String email, final String phone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.get().UPDATE_PHONE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse", "["+response+"]");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            updatePhoneListener.onPhoneUpdated(jsonObject);
                        } catch (JSONException e) {
                            updatePhoneListener.onJSONObjectException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updatePhoneListener.onVolleyErrorResponse(error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    public ConsultantsListener getConsultantsListener() {
        return consultantsListener;
    }

    public void setConsultantsListener(ConsultantsListener consultantsListener) {
        this.consultantsListener = consultantsListener;
    }

    public ConsultantListener getConsultantListener() {
        return consultantListener;
    }

    public void setConsultantListener(ConsultantListener consultantListener) {
        this.consultantListener = consultantListener;
    }

    public UpdatePhoneListener getUpdatePhoneListener() {
        return updatePhoneListener;
    }

    public void setUpdatePhoneListener(UpdatePhoneListener updatePhoneListener) {
        this.updatePhoneListener = updatePhoneListener;
    }
}
