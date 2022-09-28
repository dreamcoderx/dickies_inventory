package com.btw.dickies.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.btw.dickies.R;
import com.btw.dickies.model.ModelScan;
import com.btw.dickies.sql.ModelDbh;

public class ModelRegisterActivity
        extends AppCompatActivity
        implements View.OnClickListener {
EditText etModelNo, etProcessLoc, etProcessKanban;
Button btnSave;
    private ModelDbh modeldbh;
    private ModelScan mod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_model_register);
        etModelNo = findViewById(R.id.etRegModelNo);
        etProcessLoc = findViewById(R.id.etRegProcessLoc);
        etProcessKanban = findViewById(R.id.etRegProcessKanban);
        etModelNo.setText("");
        etProcessLoc.setText("");
        etProcessKanban.setText("");
        btnSave = findViewById(R.id.btnRegSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegSave){
            SaveModel();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    private void SaveModel(){
        modeldbh = new ModelDbh(this);
        modeldbh.open();
        mod = new ModelScan();
        mod.setModelNo(etModelNo.getText().toString());
        mod.setProcessLocation(etProcessLoc.getText().toString());
        mod.setProcessKanban(etProcessKanban.getText().toString());
        modeldbh.addModel(mod);
        modeldbh.close();

    }
}