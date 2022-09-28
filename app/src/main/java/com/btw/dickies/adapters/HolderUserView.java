package com.btw.dickies.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btw.dickies.R;

public class HolderUserView extends RecyclerView.ViewHolder {
    public TextView userid, username, email;
    public ImageView delete;

    public HolderUserView(View itemView) {
        super(itemView);
        userid = itemView.findViewById(R.id.txtUserID);
        //username = itemView.findViewById(R.id.txtname);
        //email = itemView.findViewById(R.id.txtEmail);
        //delete = itemView.findViewById(R.id.delete_emp);
    }
}
