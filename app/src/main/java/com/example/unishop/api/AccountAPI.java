package com.example.unishop.api;

import android.app.ProgressDialog;
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
import com.example.unishop.services.AccountListiner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountAPI {
    private AccountListiner accountListiner;
    private Context context;
    private ProgressDialog loading;

    public AccountAPI(Context context) {
        this.context = context;
        loading = new ProgressDialog(context);
    }

    public void loginUser(final String email, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.LOGIN_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            accountListiner.onSuccessResponse(jsonObject);
                        }catch (JSONException e){
                            accountListiner.onJSONObjectException(e);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                accountListiner.onVolleyErrorResponse(error);
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

    public void showDialogue(){
       loading.setMessage("Wait a moment...");
       loading.show();
    }

    public void hideDialogue(){
        loading.dismiss();
    }

    public AccountListiner getAccountListiner() {
        return accountListiner;
    }

    public void setAccountListiner(AccountListiner accountListiner) {
        this.accountListiner = accountListiner;
    }
}
