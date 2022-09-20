package com.btw.guess.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.btw.guess.utils.singleToneClass;
import com.btw.guess.R;

public class MenuActivity
    extends AppCompatActivity
    implements View.OnClickListener{
    Button btnScan, btnUser, btnViewScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnScan = findViewById(R.id.btnMenuScan);
        btnUser = findViewById(R.id.btnMenuUser);
        btnViewScan = findViewById(R.id.btnMenuViewScan);
        btnScan.setOnClickListener(this);
        btnUser.setOnClickListener(this);
        btnViewScan.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMenuScan){
            Intent in = new Intent(this, InventoryScanActivity.class);
            startActivity(in);

        }else if (v.getId() == R.id.btnMenuUser){
            singleToneClass sc = singleToneClass.getInstance();
            if (sc.getStrUserType().equalsIgnoreCase("ADMIN")){
                Intent in = new Intent(this, ViewUserActivity.class);
                startActivity(in);
                return;
            }
            Toast.makeText(this, "admin account only!", Toast.LENGTH_LONG).show();

        }else if (v.getId() == R.id.btnMenuViewScan){
            Intent in = new Intent(this, ScanTransactionActivity.class);
            startActivity(in);
        }
    }
}