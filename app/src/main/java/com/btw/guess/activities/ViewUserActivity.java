package com.btw.guess.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.guess.R;
import com.btw.guess.adapters.UserViewAdapter;
import com.btw.guess.model.User;
import com.btw.guess.sql.UserDbh;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class ViewUserActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    UserDbh userDH;
    ArrayList<User> allUser;
    int LAUNCH_NEW_USER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        recyclerView =  findViewById(R.id.rvModel);
        userDH = new UserDbh(this);
        displayUser();
        actionButton = (FloatingActionButton) findViewById(R.id.add);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });
    }
    public void displayUser() {
        userDH.open();
        allUser = new ArrayList<>(userDH.getAllUser());
        recyclerView = findViewById(R.id.rvModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        UserViewAdapter adapter = new UserViewAdapter(getApplicationContext(), this, allUser);
        recyclerView.setAdapter(adapter);
    }
    public void showDialog() {
        Intent i = new Intent(this, UserNewActivity.class);
        startActivityForResult(i, LAUNCH_NEW_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_NEW_USER) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                displayUser();
            }
        }
    }

}