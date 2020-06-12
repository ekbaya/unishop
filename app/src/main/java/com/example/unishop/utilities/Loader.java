package com.example.unishop.utilities;

import android.app.ProgressDialog;
import android.content.Context;

public class Loader {
    private Context context;
    private ProgressDialog loading;

    public Loader(Context context) {
        this.context = context;
        loading = new ProgressDialog(context);
    }

    public void showDialogue(){
        loading.setMessage("Please wait...");
        loading.show();
    }

    public void hideDialogue(){
        loading.dismiss();
    }
}
