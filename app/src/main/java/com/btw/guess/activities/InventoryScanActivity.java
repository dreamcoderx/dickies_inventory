package com.btw.guess.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.btw.guess.R;
import com.btw.guess.model.Scan;
import com.btw.guess.model.ScanTxn;
import com.btw.guess.sql.ScanTxnDb;
import com.btw.guess.utils.singleToneClass;

import java.text.SimpleDateFormat;

public class InventoryScanActivity
        extends AppCompatActivity {

    Button btnSave;
    EditText etRack, etRow, etBarcode, etQty;
    TextView tvTotalRack, tvStatus, tvLineNo;
    Switch swAutoSave;
    final Context ctx = this;
    int totRack = 0;
    int ctrLineNo =0;

    String curRack = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_scan);
        btnSave = findViewById(R.id.btnSave);

        etRack = findViewById(R.id.etRack);
        etRow = findViewById(R.id.etRow );
        etBarcode = findViewById(R.id.etBarcode );
        etQty = findViewById(R.id.etQty);
        tvLineNo = findViewById(R.id.tvLineNo);
        tvTotalRack = findViewById(R.id.tvTotalRack);
        tvStatus = findViewById(R.id.tvStatus);
        etRack.requestFocus();

        disableAllExcept(etRack);
        swAutoSave = findViewById(R.id.swAutoSave);

        etRack.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:

                        if (!curRack.equals(etRack.getText().toString())){
                            ctrLineNo = 0;
                            curRack = etRack.getText().toString();
                        }

                        ScanTxnDb sdb = new ScanTxnDb(ctx);
                        totRack = sdb.getTotalRack(etRack.getText().toString());
                        tvTotalRack.setText(String.valueOf(totRack));

                        tvLineNo.setText(String.valueOf(ctrLineNo));

                        disableAllExcept(etRow);
                    default:
                        break;
                }
            }
            return false;
        });

        etRow.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        disableAllExcept(etBarcode);
                    default:
                        break;
                }
            }
            return false;
        });

        etBarcode.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        if(swAutoSave.isChecked()){
                            if (saveTxn()){
                                ctrLineNo++;
                                tvLineNo.setText(String.valueOf(ctrLineNo));
                                disableAllExcept(etBarcode);
                                return false;
                            }
                        }
                        disableAllExcept(etQty);
                        return false;
                    default:
                        break;
                }
            }
            return false;
        });

        btnSave.setOnClickListener(v -> {
            try {
                if (saveTxn()){
                    ctrLineNo++;
                    tvLineNo.setText(String.valueOf(ctrLineNo));
                    if (swAutoSave.isChecked()){
                        disableAllExcept(etBarcode);
                    }
                    disableAllExcept(etRack);
                }

            }catch (Exception ex){
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        swAutoSave.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (swAutoSave.isChecked()){
                etQty.setText("1");
                if (!isComplete()){
                    swAutoSave.setChecked(false);
                    Toast.makeText(ctx, "Enter Rack and Row", Toast.LENGTH_SHORT).show();
                    return;
                }
                disableAllExcept(etBarcode);
            }
        });

        etQty.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        if (swAutoSave.isChecked()){
                            if (saveTxn()){
                                ctrLineNo++;
                                tvLineNo.setText(String.valueOf(ctrLineNo));

                                if (swAutoSave.isChecked()){
                                    disableAllExcept(etBarcode);
                                }
                                disableAllExcept(etRack);
                            }
                        }else{
                            btnSave.requestFocus();
                        }
                    default:
                        break;
                }
            }
            return false;
        });




    }

    private boolean isComplete(){
        return !etRack.getText().toString().equals("") &&
                !etQty.getText().toString().equals("") &&
                !etRow.getText().toString().equals("");
    }

    private void disableAllExcept (EditText et){
        try {
            etRack.setEnabled(false);
            etRow.setEnabled(false);
            etBarcode.setEnabled(false);
            etQty.setEnabled(false);
            et.setEnabled(true);
            et.requestFocus();
            et.setSelectAllOnFocus(true);
        }catch (Exception ex){
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // EditText etRack, etRow, etBarcode, etQty, etLineNo;
    }

    private void enableAll (){
        etRack.setEnabled(true);
        etRow.setEnabled(true);
        etBarcode.setEnabled(true);
        etQty.setEnabled(true);
        etRack.requestFocus();
    }

    private boolean saveTxn(){
        try{
            if (etRack.getText().toString().equals("") ||
                etRow.getText().toString().equals("") ||
                etBarcode.getText().toString().equals("") ||
                etQty.getText().toString().equals("")){
                    Toast.makeText(ctx, "Complete Data...", Toast.LENGTH_SHORT).show();
                return false;
            }

            int qty = Integer.parseInt(etQty.getText().toString());

            if (qty > 5000){
                Toast.makeText(ctx, "Invalid Quantity!", Toast.LENGTH_SHORT).show();
                disableAllExcept(etQty);
                return false;
            }

            singleToneClass sc = com.btw.guess.utils.singleToneClass.getInstance();
            Scan scan = new Scan();

            //scan_transaction> scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by
            //2022-09-18 00:44:58
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(System.currentTimeMillis());

            scan.setRack(etRack.getText().toString());
            scan.setRowScan(etRow.getText().toString());
            scan.setBarcode(etBarcode.getText().toString());
            scan.setLineNo(ctrLineNo);
            scan.setDateTime(date);
            scan.setQty(qty);
            scan.setScanBy(sc.getUserName());

            ScanTxnDb sdb = new ScanTxnDb(ctx);

            if (sdb.updSert(scan)){
                totRack = totRack + qty;
                tvTotalRack.setText(String.valueOf(totRack));
                tvStatus.setText("success!");
                return true;
            }else{
                tvStatus.setText("error!");
                return false;
            }

        }catch (Exception ex){
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}