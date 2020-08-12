package com.example.unishop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unishop.R;
import com.example.unishop.models.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder>{
    private Context context;
    private List<Category> categoryList;

    public CategoriesAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_category.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
         //get data
        final String id = categoryList.get(position).getId();
        final String name = categoryList.get(position).getName();
        final String description = categoryList.get(position).getDescription();

        //set Views
        holder.nameTv.setText(name);
        holder.descriptionTv.setText(description);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //views
        TextView nameTv, descriptionTv;
        CardView cdCategoryEntry;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            nameTv = itemView.findViewById(R.id.nameTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            cdCategoryEntry = itemView.findViewById(R.id.cdCategoryEntry);
        }
    }
}
