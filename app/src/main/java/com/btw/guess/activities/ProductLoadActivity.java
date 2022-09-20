package com.btw.guess.activities;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.btw.guess.model.Product;
import com.btw.guess.sql.ProductDbh;
import com.opencsv.CSVReader;
import com.btw.guess.R;

public class ProductLoadActivity extends ListActivity  {
    TextView lbl;
    Button btnimport;
    ListView lv;
    final Context context = this;
    ProgressBar pbLoadItem;
    ArrayList<HashMap<String, String>> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load);
        lbl = (TextView) findViewById(R.id.txtresulttext);
        btnimport = (Button) findViewById(R.id.btnupload);
        pbLoadItem = findViewById(R.id.pbLoadItem);
        pbLoadItem.setVisibility(View.INVISIBLE);
        lv = getListView();
        btnimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnimport.setText("Loading...");
                    new Thread(new Runnable() {
                        public void run() {
                            ProductDbh pd = new ProductDbh(context);
                            Product p = new Product();
                            //controller = new DBController(getApplicationContext());
                            //SQLiteDatabase db = controller.getWritableDatabase();
                            //db.execSQL("delete from " + DBController.tableName);
                            File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File fileToGet = new File(fileDirectory, "EPC_MASTER_DATA.csv");
                            try {
                                ContentValues contentValues = new ContentValues();
                                //db.beginTransaction();
                                if (fileToGet.exists()){
                                    CSVReader reader = new CSVReader(new FileReader(fileToGet));
                                    String[] nextLine;
                                    while ((nextLine = reader.readNext()) != null) {
                                        String company = nextLine[0];
                                        String Product = nextLine[1];
                                        String Price = nextLine[2];
                                        pd.InsertProduct(p);
                                        //contentValues.put(DBController.colCompany, company);
                                        //contentValues.put(DBController.colProduct, Product);
                                        //contentValues.put(DBController.colPrice, Price);
                                        //db.insert(DBController.tableName, null, contentValues);
                                    }
                                    //db.setTransactionSuccessful();
                                    //db.endTransaction();

                                }

                                btnimport.setText("UPLOAD");
                            } catch (IOException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("dberror", e.getMessage());
                            }
                        }
                    }).start();
                    loadProducts();
                } catch (ActivityNotFoundException e) {
                    lbl.setText("No activity can handle picking a file. Showing alternatives.");
                }
            }//btn
        });
    }

        private void loadProducts(){
            ProductDbh pd = new ProductDbh(this);
            myList = pd.getAllProducts();
            if (myList.size() != 0) {
                ListAdapter adapter = new SimpleAdapter(ProductLoadActivity.this, myList,
                        R.layout.lst_template, new String[]{"a", "b", "c"}, new int[]{
                        R.id.txtproductcompany, R.id.txtproductname, R.id.txtproductprice});
                setListAdapter(adapter);
                lbl.setText("");
            }
        }

    }

