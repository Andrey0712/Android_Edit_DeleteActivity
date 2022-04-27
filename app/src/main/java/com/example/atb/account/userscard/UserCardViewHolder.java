package com.example.atb.account.userscard;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.atb.R;

public class UserCardViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public Button btnEdit;
    public Button btnDel;
    public ImageView userimg;
    public TextView useremail;
    public UserCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view=itemView;
        userimg=itemView.findViewById(R.id.userimg);
        useremail=itemView.findViewById(R.id.useremail);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDel=itemView.findViewById(R.id.btnDel);
    }

    public View getView() {
        return view;
    }
}
