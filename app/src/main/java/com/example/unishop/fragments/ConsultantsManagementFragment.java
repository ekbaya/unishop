package com.example.unishop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.UsersActivity;
import com.example.unishop.R;
import com.example.unishop.activities.RegisterActivity;
import com.example.unishop.activities.UpdatePhoneActivity;

public class ConsultantsManagementFragment extends Fragment {
    //views
    private CardView addNewUserCard, view_consultants_cardView, updateCardView;

    //Extracting data from the intent
    private Intent intent;
    private String user_id;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultants_management, container, false);
        //init views
        addNewUserCard = view.findViewById(R.id.addNewUserCard);
        view_consultants_cardView = view.findViewById(R.id.view_consultants_cardView);
        updateCardView = view.findViewById(R.id.updateCardView);

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

        updateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePhoneActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
