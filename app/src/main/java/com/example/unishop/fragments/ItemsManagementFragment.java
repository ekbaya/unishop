package com.example.unishop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.AddProductActivity;
import com.example.unishop.R;
import com.example.unishop.activities.UpdateItemsActivity;

public class ItemsManagementFragment extends Fragment {
    //views
    private CardView addItemCard;
    private CardView updateItemsCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itmes_management, container, false);


        initViews();

        addItemCard = view.findViewById(R.id.addItemCard);
        updateItemsCard = view.findViewById(R.id.updateItemsCard);

        addItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(getActivity(), AddProductActivity.class)));
            }
        });

        updateItemsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(getActivity(), UpdateItemsActivity.class)));
            }
        });


        return view;
    }

    private void initViews() {
    }
}
