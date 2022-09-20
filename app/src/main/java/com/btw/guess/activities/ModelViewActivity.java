package com.btw.guess.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.btw.guess.adapters.ModelViewAdapter;
import com.btw.guess.model.ModelScan;
import com.btw.guess.sql.ModelDbh;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import com.btw.guess.R;

public class ModelViewActivity extends AppCompatActivity {
    RecyclerView rvModel;
    ArrayList<ModelScan> modelList;
    FloatingActionButton actionButton;
    int LAUNCH_NEW = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_view);
        rvModel = findViewById(R.id.rvModel);
        actionButton = findViewById(R.id.addModel);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        displayModel();
    }

    public void displayModel() {
        ModelDbh modDH = new ModelDbh(this);
        modDH.open();
        modelList = new ArrayList<>(modDH.getAllModel());
        rvModel = findViewById(R.id.rvModel);
        rvModel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvModel.setItemAnimator(new DefaultItemAnimator());
        ModelViewAdapter adapt = new ModelViewAdapter(getApplicationContext(), this, modelList);
        rvModel.setAdapter(adapt);
    }

    public void showDialog(){
        Intent i = new Intent(this, ModelRegisterActivity.class);
        startActivityForResult(i, LAUNCH_NEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_NEW) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                displayModel();
            }
        }
    }
}