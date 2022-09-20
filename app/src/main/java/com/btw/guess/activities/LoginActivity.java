package com.btw.guess.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.btw.guess.model.Scan;
import com.btw.guess.model.User;
import com.btw.guess.sql.DBUser;
import com.btw.guess.sql.ScanTxnDb;
import com.btw.guess.sql.UserDbh;
import com.btw.guess.utils.singleToneClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.btw.guess.R;

import java.text.SimpleDateFormat;

public class LoginActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout tilName;
    private TextInputLayout tilPassword;
    private TextInputEditText txtName;
    private TextInputEditText txtPassword;
    private Button btnLogIn, btnChangePass;

    private UserDbh usrdh;
    private User usr;
    private DBUser dbUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbUser = new DBUser(this);
        dbUser.createDatabase();
        usr = new User();
        usrdh = new UserDbh(activity);

        getSupportActionBar().hide();
        initViews();
        initListeners();
        hideKeyboardFrom(txtName);
        hideKeyboardFrom(txtPassword);

        txtName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });
        txtPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:
                            logUser();
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void testScan1(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(System.currentTimeMillis());

        Scan scan = new Scan();
        scan.setRack("1");
        scan.setRowScan("1");
        scan.setBarcode("12345");
        scan.setLineNo(1);
        scan.setDateTime(date);
        scan.setQty(6);
        scan.setScanBy("7");

        ScanTxnDb sdb = new ScanTxnDb(this);

        if (sdb.updSert(scan)){
            Toast.makeText(activity, "success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show();
        }


    }

    private void testScan2(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(System.currentTimeMillis());

        Scan scan = new Scan();
        scan.setRack("1");
        scan.setRowScan("1");
        scan.setBarcode("12346");
        scan.setLineNo(2);
        scan.setDateTime(date);
        scan.setQty(6);
        scan.setScanBy("7");

        ScanTxnDb sdb = new ScanTxnDb(this);

        if (sdb.updSert(scan)){
            Toast.makeText(activity, "success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show();
        }


    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        tilName = findViewById(R.id.textInputLayoutName);
        tilPassword = findViewById(R.id.textInputLayoutPassword);
        txtName = findViewById(R.id.textInputEditName);
        txtPassword = findViewById(R.id.textInputEditTextPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnChangePass = findViewById(R.id.btnChangePassword);

    }

    private void initListeners() {
        btnLogIn.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);
        //textViewLinkRegister.setOnClickListener(this);
        //textViewLinkUsersList.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogIn){
           logUser();
            //gmailtest();
        }else if (v.getId() == R.id.btnChangePassword){
            if (isUserValid()){
                Intent in = new Intent(this, UserChangePasswordActivity.class);
                in.putExtra("userID", usr.getUserID());
                in.putExtra("userName", usr.getUserName());
                in.putExtra("userEmail", usr.getUserEmail());
                startActivity(in);
            }
        }
    }
    private void logUser(){
        if (isUserValid()){

            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);

            singleToneClass sc = singleToneClass.getInstance();
            sc.setUserName(usr.getUserName());
            sc.setStrUserType(usr.getAcctType());

            txtName.setText("");
            txtPassword.setText("");

        }
    }

/*
    private void verifyAdmin() {

        //DatabaseHelper databaseHelper = null;
        if (databaseHelper.checkUser(textInputEditTextName.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            if(textInputEditTextName.getText().toString().equals("admin")){
                Intent intentUsersList = new Intent(getApplicationContext(), ViewUserActivity.class);
                emptyInputEditText();
                startActivity(intentUsersList);
            }else{
                Snackbar.make(nestedScrollView, "Only admin can access this!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            //Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_name_password), Snackbar.LENGTH_LONG).show();
        }
    }


 */
    private boolean isUserValid() {
        boolean rv;
        String value = txtName.getText().toString().trim();
        if (value.isEmpty()) {
            tilName.setError("Empty Name");
            hideKeyboardFrom(txtName);
            return false;
        } else {
            tilName.setErrorEnabled(false);
        }
        value = txtPassword.getText().toString().trim();
        if (value.isEmpty()) {
            tilPassword.setError("Empty Password");
            hideKeyboardFrom(txtPassword);
            return false;
        } else {
            tilName.setErrorEnabled(false);
        }

        usr.setUserID(0);
        usr.setUserName(txtName.getText().toString().trim());
        usr.setPassword(txtPassword.getText().toString().trim());
        usrdh.open();
        usr = usrdh.checkUser(usr);
        if (usr.getUserID() == 0) {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_name_password), Snackbar.LENGTH_LONG).show();
            txtName.requestFocus();
           rv = false;
        }else{
            rv = true;
        }
        usrdh.close();
        return rv;
    }


    private void emptyInputEditText() {
        txtName.setText(null);
        txtPassword.setText(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideKeyboardFrom(View view) {
        Context context;
        context = this.activity;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
