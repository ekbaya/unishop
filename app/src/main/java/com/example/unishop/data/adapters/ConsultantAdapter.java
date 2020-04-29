package com.example.unishop.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unishop.ConsultantDetailsActivity;
import com.example.unishop.R;
import com.example.unishop.data.models.ModelConsultant;

import java.util.List;

public class ConsultantAdapter extends RecyclerView.Adapter<ConsultantAdapter.MyHolder>  {
    private Context context;
    private List<ModelConsultant> consultantList;

    public ConsultantAdapter(Context context, List<ModelConsultant> consultantList) {
        this.context = context;
        this.consultantList = consultantList;
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
        final String user_id = consultantList.get(position).getUser_id();
        final String phone = consultantList.get(position).getPhone();
        final String email = consultantList.get(position).getEmail();
        final String id_number = consultantList.get(position).getId_number();
        final String firstname = consultantList.get(position).getFirstname();
        final String lastname = consultantList.get(position).getLastname();
        final String date_created = consultantList.get(position).getDate_created();
        final String created_by = consultantList.get(position).getCreated_by();

        //set Data
        holder.nameTv.setText(firstname+" "+lastname);
        holder.dateTv.setText(date_created);

        holder.cdUserEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultantDetailsActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("id_number", id_number);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("date_created", date_created);
                intent.putExtra("created_by", created_by);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return consultantList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //views
        TextView nameTv, dateTv;
        CardView cdUserEntry;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            dateTv = itemView.findViewById(R.id.dateTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            cdUserEntry = itemView.findViewById(R.id.cdUserEntry);
        }
    }
}
