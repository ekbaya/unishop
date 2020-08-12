package com.example.unishop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unishop.activities.ProfileActivity;
import com.example.unishop.R;
import com.example.unishop.models.Consultant;
import com.example.unishop.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyHolder>  {
    private Context context;
    private List<User> userList;

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_consultant.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_consultant, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        final String user_id = userList.get(position).getUid();
        final String phone = userList.get(position).getPhone();
        final String email = userList.get(position).getEmail();
        final String id_number = userList.get(position).getId_number();
        final String firstname = userList.get(position).getFirstname();
        final String lastname = userList.get(position).getLastname();
        final String created_by = userList.get(position).getEnrolledBy();

        //set Data
        holder.nameTv.setText(firstname+" "+lastname);
        holder.phoneTv.setText(phone);

        holder.cdUserEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("id_number", id_number);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("created_by", created_by);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //views
        TextView nameTv, phoneTv;
        CardView cdUserEntry;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            phoneTv = itemView.findViewById(R.id.phoneTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            cdUserEntry = itemView.findViewById(R.id.cdUserEntry);
        }
    }
}
