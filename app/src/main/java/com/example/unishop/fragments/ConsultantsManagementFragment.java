package com.example.unishop.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.UsersActivity;
import com.example.unishop.R;
import com.example.unishop.activities.RegisterActivity;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.services.AccountListener;
import com.example.unishop.utilities.Loader;
import com.google.firebase.database.DatabaseError;

import es.dmoral.toasty.Toasty;

public class ConsultantsManagementFragment extends Fragment implements AccountListener.AccountsCountListener {
    //views
    private CardView addNewUserCard, view_consultants_cardView;
    private TextView user_countTxt;
    private ProgressBar progressBar;

    //Extracting data from the intent
    private Intent intent;
    private String user_id;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;

    private AccountAPI accountAPI;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultants_management, container, false);
        //init views
        addNewUserCard = view.findViewById(R.id.addNewUserCard);
        view_consultants_cardView = view.findViewById(R.id.view_consultants_cardView);
        user_countTxt = view.findViewById(R.id.user_countTxt);
        progressBar = view.findViewById(R.id.progressBar);

        accountAPI = new AccountAPI(getActivity());
        accountAPI.setAccountsCountListener(this);
        accountAPI.getUsersCounts();


        intent = getActivity().getIntent();
        user_id = intent.getStringExtra("user_id");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");

        addNewUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        view_consultants_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UsersActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onAccountCountReceived(int count) {
        user_countTxt.setText(Integer.toString(count));
        progressBar.setVisibility(View.GONE);
        user_countTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
       Log.e("COUNT ERROR: ", error.getMessage());

    }
}
