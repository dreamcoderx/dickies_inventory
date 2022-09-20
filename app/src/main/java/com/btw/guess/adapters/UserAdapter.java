package com.btw.guess.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.guess.model.UserView;
import com.btw.guess.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserView> userList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userid, username, email;
        public MyViewHolder(View view) {
            super(view);
            userid = (TextView) view.findViewById(R.id.tvuserid);
            username = (TextView) view.findViewById(R.id.tvusername);
            email = (TextView) view.findViewById(R.id.tvemail);
        }
    }


    public UserAdapter(List<UserView> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserView user = userList.get(position);
        holder.userid.setText(String.valueOf(user.getUserid()));
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
