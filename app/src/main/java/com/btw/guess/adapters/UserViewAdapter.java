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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.guess.model.User;
import com.btw.guess.sql.UserDbh;
import com.google.android.material.textfield.TextInputEditText;
import com.btw.guess.R;

import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.viewHolder> {

    Context context;
    AppCompatActivity activity;
    ArrayList<User> arrayList;
    UserDbh userDH;

    public UserViewAdapter(Context context, AppCompatActivity activity, ArrayList<User> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.userid.setText(String.valueOf(arrayList.get(position).getUserID()));
        holder.username.setText(arrayList.get(position).getUserName());
        holder.email.setText(arrayList.get(position).getUserEmail());
        userDH = new UserDbh(context);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {

                if (arrayList.get(position).getUserName().trim().equals("admin") ){
                    Toast.makeText(context, "cannot delete admin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                userDH.open();
                userDH.deleteUserID(arrayList.get(position).getUserID());
                userDH.close();
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
        TextView userid, username, email;
        Button btnEdit, btnDelete;


        public viewHolder(View itemView) {
            super(itemView);
            userid = (TextView) itemView.findViewById(R.id.tvuserid);
            username = (TextView) itemView.findViewById(R.id.tvusername);
            email = (TextView) itemView.findViewById(R.id.tvemail);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
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

        username.setText(arrayList.get(pos).getUserName());
        useremail.setText(arrayList.get(pos).getUserEmail());


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
                    usr.setUserID(arrayList.get(pos).getUserID());
                    usr.setUserName(username.getText().toString());
                    usr.setUserEmail(useremail.getText().toString());
                    usr.setPassword(password.getText().toString());
                    userDH.open();
                    userDH.updateUser(usr);
                    arrayList.get(pos).setUserName(username.getText().toString());
                    arrayList.get(pos).setUserEmail(useremail.getText().toString());
                    dialog.cancel();
                    notifyDataSetChanged();
                }
            }
        });
    }
}