package com.btw.guess.activities;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.btw.guess.sql.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.TriggerStateChangeEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.btw.guess.R;

public class MainActivity
        extends AppCompatActivity
        implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private DatabaseHelper databaseHelper;
    private static TextInputEditText textInputEditTextLocation;
    private static TextInputEditText textInputEditTextParts;
    private static AppCompatTextView textViewStatus;
    private static NestedScrollView nestedScrollView;
    private static TextInputLayout textInputLayoutLocation;
    private static TextInputLayout textInputLayoutParts;


    private static String strUser;
// get bar code instance from MainActivity
    private static void startScan(){
        textInputEditTextLocation.setEnabled(true);
        textInputEditTextParts.setEnabled(true);
        textInputEditTextLocation.setText("");
        textInputEditTextParts.setText("");
        textInputEditTextLocation.requestFocus();

    }
    public void errorTone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void WriteTextfile(String strKanban, String strLocation, String strActual) {

        String statusResult;
        if (!strActual.substring(0,5).equals(strKanban.substring(0,5))){
            statusResult = "MISMATCH";
            errorTone();
        }else if (!strActual.substring(0,5).equals(strLocation.substring(0,5))){
            statusResult = "MISMATCH" ;
            errorTone();}
        else{
            statusResult = "OK";
        }

        textViewStatus.setText(statusResult);

        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String writeData = strKanban + ", " + strLocation + ", " + strActual + ", " + statusResult + ", " + dateStr + "," + strUser + "\r\n";

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File myFile = new File(dir ,  "scandata.csv");

        if(myFile.exists())
        {
            try
            {

                FileWriter textFileWriter = new FileWriter(myFile, true);
                BufferedWriter out = new BufferedWriter(textFileWriter);
                out.write(writeData);
                out.close();
                Log.d("file create", "File Exists: " + writeData + "/" + myFile.getName());
                textViewStatus.setText(statusResult);
            } catch(Exception e) {
                textViewStatus.setText("FILE UPDATE ERROR!");
                Log.e("file create", "error:" + e.getMessage());
            }
        }
        else
        {
            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(myFile, true));
                String headerData = "Kanban, Location, Actual, Result, TimeStamp, Username" + "\r\n";
                writeData = headerData + writeData;
                out.write(writeData);
                out.close();
                Log.d("file create", myFile.getName());
            } catch (IOException e) {
                textViewStatus.setText("FILE CREATE ERROR!");
                Log.e("file create", e.getMessage());

            }
        }
    }

    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);
        textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        textInputLayoutParts = findViewById(R.id.textInputLayoutParts);
        textInputEditTextLocation = findViewById(R.id.textInputEditTextLocation);
        textInputEditTextParts = findViewById(R.id.textInputEditTextParts);
        textViewStatus = findViewById(R.id.textViewStatus);
        strUser = getIntent().getStringExtra("NAME");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        startScan();
        databaseHelper = new DatabaseHelper(this);
       // ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textInputEditTextLocation.getWindowToken(), 0);



        textViewStatus.setText("get barcode object");



        /*
        textInputEditTextKanban.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getApplicationContext(),KeyEvent.keyCodeToString(keyCode),Toast.LENGTH_SHORT).show();
                    switch (keyCode)
                    {

                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:

                            if (textInputEditTextKanban.getText().length() < 13) {
                                textViewStatus.setText("INVALID KANBAN!");
                                textInputEditTextKanban.setError("INVALID KANBAN!");
                                //hideKeyboard(this);
                                //hideKeyboardFrom(textInputEditTextKanban);
                                errorTone();
                                startScan();
                            }
                            else{
                                textViewStatus.setText("OK KANBAN!");
                                textInputEditTextLocation.setEnabled(true);
                                textInputEditTextLocation.setFocusable(true);
                                textInputEditTextLocation.setFocusableInTouchMode(true);
                                textInputEditTextLocation.requestFocus();

                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

         */

        /*
        textInputEditTextLocation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getApplicationContext(),KeyEvent.keyCodeToString(keyCode),Toast.LENGTH_SHORT).show();
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:
                            if (textInputEditTextLocation.getText().length() != 5){
                                textViewStatus.setText("INVALID LOCATION!");
                                textInputEditTextLocation.setError("INVALID LOCATION!");
                                errorTone();
                                startScan();
                            }else if (!textInputEditTextLocation.getText().toString().substring(0,5).equals(textInputEditTextKanban.getText().toString().substring(0,5))) {
                                textViewStatus.setText("MISMATCH!");
                                textInputEditTextLocation.setError("MISMATCH!");
                                errorTone();
                                startScan();
                            }
                            else{
                                textViewStatus.setText("OK LOCATION");
                                 textInputEditTextActual.setEnabled(true);
                                textInputEditTextActual.requestFocus();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        textInputEditTextActual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (textInputEditTextActual.getText().length() >= 13){
                        WriteTextfile(textInputEditTextKanban.getText().toString(), textInputEditTextLocation.getText().toString(), textInputEditTextActual.getText().toString());
                    }
                    else{
                        errorTone();
                        textViewStatus.setText("INVALID ACTUAL!");
                    }
                    startScan();
                }
            }
        });

 */
    }






    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // update UI to reflect the data
                List<String> list = new ArrayList<String>();
                list.add("Barcode data: " + event.getBarcodeData());
                list.add("Character Set: " + event.getCharset());
                list.add("Code ID: " + event.getCodeId());
                list.add("AIM ID: " + event.getAimId());
                list.add("Timestamp: " + event.getTimestamp());
                String barScan = event.getBarcodeData();
                textViewStatus.setText(barScan);
                //textViewStatus.setText(event.getBarcodeData().toString());

                if (textInputEditTextLocation.hasFocus()){
                    textInputEditTextLocation.setText(barScan);
                    if (barScan.length() != 5) {
                        textViewStatus.setText("Invalid Location");
                        errorTone();
                        return;
                    }

                    textInputEditTextParts.requestFocus();
                }else if(textInputEditTextParts.hasFocus()) {
                    textInputEditTextParts.setText(barScan);

                    if (barScan.length() != 13) {
                        textViewStatus.setText("Invalid Parts Barcode");
                        errorTone();
                        return;
                    }

                    if (textInputEditTextLocation.getText().toString().substring(0,5).equals(barScan) ){
                        textViewStatus.setText("MODEL NUMBER MISMATCH");
                        errorTone();
                        //showDialog();
                        return;
                    }
                        textInputEditTextLocation.requestFocus();

                }



                //Toast.makeText(getApplicationContext(),event.getBarcodeData().toString(),Toast.LENGTH_SHORT).show();
                //final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                //        AutomaticBarcodeActivity.this, android.R.layout.simple_list_item_1, list);
                //barcodeList.setAdapter(dataAdapter);
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
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

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}