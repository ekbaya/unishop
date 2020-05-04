package com.example.unishop.ui.items_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.AddProductActivity;
import com.example.unishop.R;

public class ItemsManagementFragment extends Fragment {
    //views
    private CardView addItemCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itmes_management, container, false);

        addItemCard = view.findViewById(R.id.addItemCard);

        addItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(getActivity(), AddProductActivity.class)));
            }
        });

        return view;
    }
}
