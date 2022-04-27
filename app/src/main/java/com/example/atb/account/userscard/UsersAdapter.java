package com.example.atb.account.userscard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.atb.R;
import com.example.atb.application.HomeApplication;
import com.example.atb.constants.Urls;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserCardViewHolder> {
    private List<UserDTO> users;
    private final OnUserClickListener listener;
    private final OnUserClickListener editListener;
    private final OnUserClickListener delListener;
    public UsersAdapter(List<UserDTO> users, OnUserClickListener listener,
                        OnUserClickListener editListener,OnUserClickListener delListener) {
        this.users = users;
        this.listener = listener;
        this.editListener=editListener;
        this.delListener=delListener;
    }

    @NonNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_user, parent, false);
        return new UserCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCardViewHolder holder, int position) {
        if(users!=null && position<users.size())
        {
            UserDTO user = users.get(position);
            holder.useremail.setText(user.getEmail());
            String url = Urls.BASE+user.getPhoto();
            Glide.with(HomeApplication.getAppContext())
                    .load(url)
                    .apply(new RequestOptions().override(600, 300))
                    .into(holder.userimg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editListener.onItemClick(user);
                }
            });
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delListener.onItemClick(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
