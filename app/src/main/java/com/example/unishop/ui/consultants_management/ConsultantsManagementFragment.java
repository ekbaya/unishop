package com.example.unishop.ui.consultants_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.AdminHomeActivity;
import com.example.unishop.R;
import com.example.unishop.RegisterActivity;

public class ConsultantsManagementFragment extends Fragment {
    //views
    private CardView addNewUserCard;

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
                intent.putExtra("user_id",user_id);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("firstname",firstname);
                intent.putExtra("lastname",lastname);
                startActivity(intent);
            }
        });

        return view;
    }
}
