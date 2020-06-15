package com.example.unishop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.CategoriesActivity;
import com.example.unishop.R;
import com.example.unishop.activities.ProductsActivity;
import com.example.unishop.activities.RemoveItemActivity;
import com.example.unishop.activities.UpdateItemsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsManagementFragment extends Fragment implements View.OnClickListener {
    //views
    @BindView(R.id.addItemCard) CardView addItemCard;
    @BindView(R.id.updateItemsCard) CardView updateItemsCard;
    @BindView(R.id.viewItemsCard) CardView viewItemsCard;
    @BindView(R.id.removeItemCard) CardView removeItemCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itmes_management, container, false);
        ButterKnife.bind(this, view);
        addItemCard.setOnClickListener(this);
        updateItemsCard.setOnClickListener(this);
        viewItemsCard.setOnClickListener(this);
        removeItemCard.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
       if (v.equals(addItemCard)){
           startActivity(new Intent(new Intent(getActivity(), CategoriesActivity.class)));
       }
       if (v.equals(updateItemsCard)){
           startActivity(new Intent(new Intent(getActivity(), UpdateItemsActivity.class)));
       }
       if (v.equals(viewItemsCard)){
           startActivity(new Intent(new Intent(getActivity(), ProductsActivity.class)));
       }
        if (v.equals(removeItemCard)){
            startActivity(new Intent(new Intent(getActivity(), RemoveItemActivity.class)));
        }
    }
}
