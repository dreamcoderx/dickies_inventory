package com.btw.guess.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.guess.model.ModelScan;
import com.btw.guess.model.User;
import com.btw.guess.sql.ModelDbh;
import com.google.android.material.textfield.TextInputEditText;
import com.btw.guess.R;

import java.util.ArrayList;

public class ModelViewAdapter extends RecyclerView.Adapter<ModelViewAdapter.viewHolder> {

    Context context;
    AppCompatActivity activity;
    ArrayList<ModelScan> arrayList;
    ModelDbh modelDH;


    public ModelViewAdapter(Context context, AppCompatActivity activity, ArrayList<ModelScan> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_row_model, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvModelNo.setText(arrayList.get(position).getModelNo());
        holder.tvProcessLoc.setText(arrayList.get(position).getProcessLocation());
        holder.tvProcessKanban.setText(arrayList.get(position).getProcessKanban());
        modelDH = new ModelDbh(context);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting user
                //database_helper.deleteUserID(arrayList.get(position).getUserid());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvModelNo, tvProcessLoc, tvProcessKanban;
        Button btnEdit, btnDelete;


        public viewHolder(View itemView) {
            super(itemView);
            tvModelNo = (TextView) itemView.findViewById(R.id.tvModelNo);
            tvProcessLoc = (TextView) itemView.findViewById(R.id.tvProcessLocation);
            tvProcessKanban = (TextView) itemView.findViewById(R.id.tvProcessKanban);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeleteModel);
            btnEdit = (Button) itemView.findViewById(R.id.btnEditModel);
         }
    }

    public void showDialog(final int pos) {
        final TextInputEditText username, useremail, password, confirmpassword;
        final AppCompatButton submit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

         username = (TextInputEditText) dialog.findViewById(R.id.textInputEditName);
         useremail = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextEmail);
         password = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextPassword);
         confirmpassword = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextConfirmPassword);
        submit = (AppCompatButton) dialog.findViewById(R.id.btnSaveUser);

        //username.setText(arrayList.get(pos).getUsername());
        //useremail.setText(arrayList.get(pos).getEmail());


        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    username.setError("Please Enter Username");
                }else if(useremail.getText().toString().isEmpty()) {
                    useremail.setError("Please Enter User Email");
                }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password!");
                }else if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    confirmpassword.setError("Password is not equal!");
                }else {
                    User usr = new User();
                    //usr.setId(arrayList.get(pos).getUserid());
                    //usr.setUserID(arrayList.get(pos));
                    //usr.setUserName(username.getText().toString());
                    //usr.setUserEmail(useremail.getText().toString());
                    //usr.setPassword(password.getText().toString());
                    //userDH.updateUser(usr);
                    //arrayList.get(pos).setUsername(username.getText().toString());
                    //arrayList.get(pos).setEmail(useremail.getText().toString());
                   // notifyDataSetChanged();
                    dialog.cancel();
                    notifyDataSetChanged();
                }
            }
        });
    }
}