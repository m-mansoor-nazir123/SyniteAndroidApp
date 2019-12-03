package com.example.firebasechatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends  RecyclerView.Adapter<AdapterUser.MyHolder> {

Context context;
List<ModelUsers> UsersList;

    public AdapterUser(Context context, List<ModelUsers> usersList) {
        this.context = context;
        UsersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String UserEmail=UsersList.get(position).getEmail();
        final String HisUid=UsersList.get(position).getUid();
        holder.email.setText(UserEmail);

        //Picasso.get().load(user)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hisuid",HisUid);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView email;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.avatarIv);
            email=itemView.findViewById(R.id.nameTv);



        }
    }
}
