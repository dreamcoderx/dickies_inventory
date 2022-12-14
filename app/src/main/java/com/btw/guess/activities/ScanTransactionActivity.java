package com.btw.guess.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.btw.guess.R;
import com.btw.guess.adapters.ScanAdapter;
import com.btw.guess.adapters.ScanTransactionAdapter;
import com.btw.guess.model.Scan;
import com.btw.guess.sql.ScanTxnDb;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;

public class ScanTransactionActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    Button btnDateFrom, btnDateTo, btnQuery, btnSaveCSV;
    RecyclerView rView;
    EditText etFromDate, etToDate;
    List<Scan> listScan;
    ScanAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_transaction);
        btnDateFrom = findViewById(R.id.btnScanSelFrom);
        btnDateTo = findViewById(R.id.btnScanSelTo);
        btnQuery = findViewById(R.id.btnScanQuery);
        btnSaveCSV = findViewById(R.id.btnScanSaveCSV);
        etFromDate = findViewById(R.id.etScanDateFrom);
        etToDate = findViewById(R.id.etScanDateTo);
        rView = findViewById(R.id.recViewScanTxn);
        btnDateFrom.setOnClickListener(this);
        btnDateTo.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnSaveCSV.setOnClickListener(this);
    }

    public void loadRecyclerView() {
        try {
            ScanTxnDb s = new ScanTxnDb(this);
            listScan = new ArrayList<>(s.getScanByDate(etFromDate.getText().toString(), etToDate.getText().toString()));
            rView.setHasFixedSize(true);
            rView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ScanAdapter(this, R.layout.custom_list_item, listScan);
            rView.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnScanQuery){
            loadRecyclerView();
        } else if(v.getId()== R.id.btnScanSelFrom){
            setDate(etFromDate);
        } else if(v.getId()==R.id.btnScanSelTo){
            setDate(etToDate);
        } else if(v.getId()== R.id.btnScanSaveCSV){
            String s = etFromDate.getText() + " to " + etToDate.getText();
            s = s + ".dat";
            if (createCsv(s)){
                Toast.makeText(this, s + "- created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDate(EditText et){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year , monthOfYear , dayOfMonth);
                        //c.roll(Calendar.DATE, 1);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String datestring=format.format(c.getTime());
                        et.setText(datestring);
                    }
                }, year, month, day);
        picker.show();
    }

    private boolean saveData(){
        boolean rv = false;
        try {
            String path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            File file = new File(path , "scan.dat");

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");
            String curDateTime = sdf.format(new Date());

            String strRack, strRow, strBarcode, strQty, strData;
            //101 ,1   ,1111117966857       ,1     ,04/11/2022,22:49:48
            //rack,row, barcode, qty, date, time (MM/dd/yyyy, HH:mm:ss)
            //101 ,1   ,1111117966857       ,1     ,04/11/2022,22:49:48

            //strRack = etRack.getText().toString();
            //strRow = etRow.getText().toString();
            //strBarcode = etBarcode.getText().toString();
            //strQty = etQty.getText().toString();
            //strLineNo = etLineNo.getText().toString();


            //strRack = String.format("%-4s",strRack);
            //strRow = String.format("%-4s",strRow);
            //strLineNo = String.format("%-4s",strLineNo);
            //strBarcode = String.format("%-20s",strBarcode);
            //strQty = String.format("%-6s",strQty);


            //strData = strRack+ "," + strRow + ","  + strBarcode + ","  + strQty + "," + curDateTime;

            FileWriter writer = new FileWriter(file, true);
            //writer.append(strData + "\n");

            writer.flush();
            writer.close();

            Toast.makeText(this, "Successfully Save to " + file.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();


            rv= true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return rv;
    }

    private boolean createCsv(String filename){
        boolean rv = false;
        String path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File file = new File(path , filename);
        if(file.exists()) {
            file.delete();
        }
        String strRack, strRow, strBarcode, strQty, strDateTime;

        try {
            FileWriter writer = new FileWriter(file);
            //writer.append("KANBAN,  LOCATION, DATE TIME, SCAN BY, LINE LEADER, STATUS \n");
            for (int i = 0; i < listScan.size(); i++) {
                String w = "";

                //101 ,1   ,1111117966857       ,1     ,04/11/2022,22:49:48
                strBarcode = listScan.get(i).getBarcode();
                strQty = String.valueOf(listScan.get(i).getQty()) ;
                strDateTime = "";//arrList.get(i).getDate_time();

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date newDate = sdf.parse(strDateTime);
                    sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
                    strDateTime= sdf.format(newDate);
                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }

                strBarcode = String.format("%-20s",strBarcode);
                strQty = String.format("%-6s",strQty);

                w = cp(w, strBarcode);
                w = cp(w, strQty);
                w = cp(w, strDateTime);
                w = w + "\n";
                writer.append(w);
            }
            writer.flush();
            writer.close();
            Toast.makeText(this, file.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
            rv= true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return rv;
    }

    private String cp(String out, String in){
        if (out == ""){
            out = in;
        }else{
            out = out + ", " + in;
        }
        return out;
    }
}