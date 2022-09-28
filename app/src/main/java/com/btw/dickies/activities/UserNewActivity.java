package com.btw.dickies.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.btw.dickies.R;
import com.btw.dickies.model.User;
import com.btw.dickies.sql.UserDbh;

public class UserNewActivity extends AppCompatActivity {
    EditText name, email, password, confirmpassword;
    Context ctx;
    AppCompatButton register;
    Spinner spnrAcctType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new);
        ctx = this;
        name =  findViewById(R.id.textInputEditName);
        email = findViewById(R.id.textInputEditTextEmail);
        password = findViewById(R.id.textInputEditTextPassword);
        confirmpassword = findViewById(R.id.textInputEditTextConfirmPassword);
        spnrAcctType = findViewById(R.id.spnrAcct);
        register = findViewById(R.id.btnSaveUser);

        loadUserType();

        register.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter Username");
                }else if(email.getText().toString().isEmpty()) {
                    email.setError("Please Enter User Email");
                }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password!");
                }else if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    confirmpassword.setError("Password is not equal!");
                }else {
                    User usr = new User();
                        usr.setUserName(name.getText().toString());
                        usr.setUserEmail(email.getText().toString());
                        usr.setPassword(password.getText().toString());
                        usr.setAcctType(spnrAcctType.getSelectedItem().toString());

                    UserDbh userDH = new UserDbh(ctx);
                    userDH.open();
                    userDH.addUser(usr);
                    userDH.close();
                    //dialog.cancel();
                    //displayUser();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            }
        });

    }
    private void loadUserType(){
        String userType[] = {"Admin","User", "LineLeader"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, userType);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnrAcctType.setAdapter(spinnerArrayAdapter);
    }
}