package com.example.unishop.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.R;
import com.example.unishop.utilities.SharedHelper;
import com.example.unishop.services.AccountListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountAPI {
    private AccountListener accountListener;
    private Context context;

    public AccountAPI(Context context) {
        this.context = context;
    }

    public void loginUser(final String email, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.LOGIN_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            accountListener.onSuccessResponse(jsonObject);
                        }catch (JSONException e){
                            accountListener.onJSONObjectException(e);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                accountListener.onVolleyErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void registerNewUser(final String first_name, final String last_name, final String u_email,
                                 final String u_phone, final String u_id_number, final String password, final String user_role){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.REGISTER_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            accountListener.onSuccessResponse(jsonObject);
                        } catch (JSONException e) {
                            accountListener.onJSONObjectException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        accountListener.onVolleyErrorResponse(error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", first_name);
                params.put("lastname", last_name);
                params.put("id_number", u_id_number);
                params.put("email", u_email);
                params.put("password", password);
                params.put("phone", u_phone);
                params.put("role", user_role);
                params.put("user_id", SharedHelper.getKey(context,"user_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public AccountListener getAccountListener() {
        return accountListener;
    }

    public void setAccountListener(AccountListener accountListener) {
        this.accountListener = accountListener;
    }
}
