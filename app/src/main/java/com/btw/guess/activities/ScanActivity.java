package com.btw.guess.activities;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.btw.guess.R;
import com.btw.guess.adapters.CountTxnAdaptVw;
import com.btw.guess.model.Scan;
import com.btw.guess.model.ScanTxn;
import com.btw.guess.model.User;
import com.btw.guess.sql.ScanDbh;
import com.btw.guess.sql.ScanTxnDb;
import com.btw.guess.sql.UserDbh;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanActivity
    extends AppCompatActivity {

    private EditText etScanBarcode;
    private RecyclerView rcView;
    ScanDbh scanDh;
    Scan scan = new Scan();
    Ringtone rTone;
    ArrayList<ScanTxn> arrList;
    String salesOrderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanDh = new ScanDbh(this);
        scanDh.open();
        etScanBarcode = findViewById(R.id.etScanBarcode);
        rcView = findViewById(R.id.rvTxn);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        rTone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        etScanBarcode.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        if (etScanBarcode.getText().toString().trim().length() == 0) {
                            break;
                        }
                        procBarcode();
                    default:
                        break;
                }
            }
            return false;
        });
    }
    public void loadRecyclerView() {
        ScanTxnDb s = new ScanTxnDb(this);
        //arrList = new ArrayList<>(s.getAllScan(salesOrderNo));
        rcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcView.setItemAnimator(new DefaultItemAnimator());
        CountTxnAdaptVw adapt = new CountTxnAdaptVw(getApplicationContext(), this, arrList);
        Toast.makeText(this, valueOf(adapt.getItemCount()), Toast.LENGTH_LONG).show();
        rcView.setAdapter(adapt);
        s.close();
    }
    public void errorTone(){
        try {
            rTone.play();
            dialogUserPass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTone(){
        try {
            rTone.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void procBarcode(){
        //A803L025BP20623R0001CQN045
        if (etScanBarcode.getText().length() < 26) {
            //errorTone();
            Toast.makeText(this, "INVALID BARCODE", Toast.LENGTH_SHORT).show();
            etScanBarcode.requestFocus();
        }
        else{
            if (isBarcodeAlreadyScan(etScanBarcode.getText().toString())){
                Toast.makeText(this, "ALREADY SCAN", Toast.LENGTH_SHORT).show();
                etScanBarcode.requestFocus();
            }else{
                saveTxn();
                loadRecyclerView();
                etScanBarcode.setText("");
                etScanBarcode.requestFocus();
            }

        }
    }
    private void saveTxn(){
        try {
            ScanDbh s = new ScanDbh(this);
            s.open();
            scan.setBarcode(etScanBarcode.getText().toString());

            String barcode = etScanBarcode.getText().toString();
            //A803L025BP20623R0001CQN045
            salesOrderNo = barcode.substring(20,barcode.length()-1);
            //scan.setSalesOrderNo(salesOrderNo);

            if (s.saveTxn(scan)){
                Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();
                goodTone();
                s.close();
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isBarcodeAlreadyScan(String barcode){
        boolean rv = false;
        try {
            ScanDbh s = new ScanDbh(this);
            s.open();
            if (s.isBarcodeExists(barcode)){
                rv = true;
            }
            s.close();
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return rv;
    }
    private void dialogUserPass(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("LINE SUPERVISOR");
        final View customLayout = getLayoutInflater().inflate(R.layout.user_pass, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                TextInputEditText txtUser = customLayout.findViewById(R.id.txtUser);
                TextInputEditText txtPassword = customLayout.findViewById(R.id.txtPassword);
                StopError(txtUser.getText().toString(), txtPassword.getText().toString());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    private void StopError(String user, String password) {
        if (user.equals("") || password.equals("")){
           dialogUserPass();
        }

        User usr = new User();
        usr.setUserID(0);
        usr.setUserName(user);
        usr.setPassword(password);

        UserDbh ud = new UserDbh(this);

        if (!ud.isValidAdmin(usr)){
            dialogUserPass();
            return;
        }
        saveTxn();
        stopTone();
    }
    public void goodTone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}