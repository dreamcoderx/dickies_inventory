package com.btw.dickies.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.btw.dickies.R;
import com.btw.dickies.model.User;
import com.btw.dickies.sql.UserDbh;
import com.google.android.material.snackbar.Snackbar;
public class UserChangePasswordActivity
        extends AppCompatActivity
        implements View.OnClickListener{
EditText etUsername, etPassword, etConfirmPassword, etEmail;
Button btnSave;
UserDbh usrdh;
int UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        UserID = getIntent().getIntExtra("userID",0);
        usrdh = new UserDbh(this);

        etUsername = findViewById(R.id.etEditUsername);
        etEmail = findViewById(R.id.etEditUserEmail);
        etPassword = findViewById(R.id.etEditUserPassword);
        etConfirmPassword = findViewById(R.id.etEditUserConfirmPassword);

       etUsername.setText(getIntent().getStringExtra("userName"));
       etEmail.setText(getIntent().getStringExtra("userEmail"));
        btnSave = findViewById(R.id.btnEditUserUpdate);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEditUserUpdate){
            if (!(etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim()))){
                Snackbar.make(etPassword,"Password Not Match!", Snackbar.LENGTH_LONG).show();
                return;
            }
            saveUser();
        }

    }

    private void saveUser(){
        User usr = new User();
        usr.setUserID(UserID);
        usr.setUserName(etUsername.getText().toString());
        usr.setUserEmail(etEmail.getText().toString());
        usr.setPassword(etPassword.getText().toString());

        usrdh.open();
        boolean success = usrdh.updateUser(usr);
        if (!success){
            Snackbar.make(etPassword,usrdh.getMessage(), Snackbar.LENGTH_LONG).show();
        } else{
            Snackbar.make(etPassword,"Update Success!", Snackbar.LENGTH_LONG).show();
        }
        usrdh.close();
    }
}