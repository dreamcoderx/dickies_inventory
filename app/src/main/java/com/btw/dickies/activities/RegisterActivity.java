package com.btw.dickies.activities;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import com.btw.dickies.helpers.InputValidation;
import com.btw.dickies.model.User;
import com.btw.dickies.sql.UserDbh;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.btw.dickies.R;
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private UserDbh userDH;
    private User usr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
        textInputEditTextConfirmPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:
                            postDataToSQLite();
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        tilName = findViewById(R.id.textInputLayoutName);
        tilEmail = findViewById(R.id.textInputLayoutEmail);
        tilPassword = findViewById(R.id.textInputLayoutPassword);
        tilConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = findViewById(R.id.textInputEditName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = findViewById(R.id.btnSaveUser);

        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }
    private void initObjects() {
        //inputValidation = new InputValidation(activity);
        userDH = new UserDbh(activity);
        usr = new User();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSaveUser:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {

        String value = textInputEditTextName.getText().toString().trim();
        if (value.isEmpty()) {
            tilName.setError("Empty Name");
            hideKeyboardFrom(textInputEditTextName);
            return;
        } else {
            tilName.setErrorEnabled(false);
        }

        value = textInputEditTextPassword.getText().toString().trim();

        if (value.isEmpty()) {
            tilEmail.setError("Empty Email");
            hideKeyboardFrom(textInputEditTextEmail);
            return;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        value = textInputEditTextPassword.getText().toString().trim();

        if (value.isEmpty()) {
            tilPassword.setError("Empty Password");
            hideKeyboardFrom(textInputEditTextPassword);
            return;
        } else {
            tilPassword.setErrorEnabled(false);
        }

        String pw = textInputEditTextPassword.getText().toString().trim();
        String con_pw = textInputEditTextConfirmPassword.getText().toString().trim();
        if (!pw.contentEquals(con_pw)) {
            tilConfirmPassword.setError("Password Not Match!");
            hideKeyboardFrom(textInputEditTextConfirmPassword);
            return ;
        } else {
            tilConfirmPassword.setErrorEnabled(false);
        }

        userDH.open();
        User usr = new User();
        usr.setUserEmail(textInputEditTextEmail.getText().toString().trim());
        if (!userDH.checkUserEmail(usr)) {
            usr.setUserName(textInputEditTextName.getText().toString().trim());
            usr.setUserEmail(textInputEditTextEmail.getText().toString().trim());
            usr.setPassword(textInputEditTextPassword.getText().toString().trim());
            userDH.addUser(usr);
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
        userDH.close();


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

    private void hideKeyboardFrom(View view) {
        Context context;
        context = this.activity;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
