package com.btw.dickies.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.dickies.model.SimpleModel;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
    private TextView textUserid;

    public void bindData(final SimpleModel viewModel) {
        textUserid.setText(viewModel.getUserid());
        //textUsername.setText(viewModel.getUsername());
        //textEmail.setText(viewModel.getEmail());

    }
    public SimpleViewHolder(final View itemView) {
        super(itemView);
        //simpleTextView = (TextView) itemView.findViewById(R.id.simple_text);
    }
}