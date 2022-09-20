package com.btw.guess.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.guess.R;
import com.btw.guess.model.Scan;
import com.btw.guess.sql.ScanTxnDb;
import com.btw.guess.utils.singleToneClass;


import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {

    int custom_list_item;
    SQLiteDatabase mDatabase;


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Scan> scanList;

    //getting the context and product list with constructor
    public ScanAdapter(Context mCtx, int custom_list_item, List<Scan> scanList) {
        this.mCtx = mCtx;
        this.custom_list_item = custom_list_item;
        this.mDatabase = mDatabase;
        this.scanList = scanList;
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_list_item, null);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.ScanViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //getting the product of the specified position
        final Scan scan = scanList.get(position);

        //TextView tvRVScanID, tvRVScanRack, tvRVScanRow, tvRVScanBarcode;
        //ImageView btnScanEdit, btnScanDelete;

        //binding the data with the viewholder views
        holder.tvRVScanID.setText(String.valueOf(scan.getScanID()));
        holder.tvRVScanRack.setText(scan.getRack());
        holder.tvRVScanRow.setText(scan.getRowScan());
        holder.tvRVScanBarcode.setText(scan.getBarcode());
        holder.tvRVScanLineNo.setText(String.valueOf(scan.getLineNo()));
        holder.tvRVScanItemQty.setText(String.valueOf(scan.getQty()));

        holder.btnScanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQty(scan);
            }
        });

        holder.btnScanDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScanTxnDb sdb = new ScanTxnDb(mCtx);
                        if (sdb.deleteScan(scan.getScanID())){
                            if (sdb.updateDelete(scan)){
                                Toast.makeText(mCtx, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                scanList.remove(position);
                                notifyDataSetChanged();
                            }
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void updateQty(Scan scan) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_qty, null);
        builder.setView(view);

        final EditText editQty = view.findViewById(R.id.etUpdateQty);

        editQty.setText(String.valueOf(scan.getQty()));
        final AlertDialog dialog = builder.create();
        dialog.show();

        // CREATE METHOD FOR EDIT THE FORM
        view.findViewById(R.id.btnUpdateQty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qtyEtValue = editQty.getText().toString().trim();
                if (qtyEtValue.isEmpty()) {
                    editQty.setError("qty can't be blank!");
                    editQty.requestFocus();
                    return;
                }

                singleToneClass sc = com.btw.guess.utils.singleToneClass.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(System.currentTimeMillis());

                scan.setDateTime(date);
                scan.setScanBy(sc.getUserName());
                int qty = Integer.parseInt(qtyEtValue);
                scan.setQty(qty);

                ScanTxnDb sdb = new ScanTxnDb(mCtx);
                if (sdb.updateQty(scan)){
                   Toast.makeText(mCtx, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }


    class ScanViewHolder extends RecyclerView.ViewHolder {

        TextView tvRVScanID, tvRVScanRack, tvRVScanRow;
        TextView tvRVScanItemQty, tvRVScanBarcode, tvRVScanLineNo;
        ImageView btnScanEdit, btnScanDelete;

        public ScanViewHolder(View itemView) {
            super(itemView);
            tvRVScanID = itemView.findViewById(R.id.tvRVScanID);
            tvRVScanRack = itemView.findViewById(R.id.tvRVScanRack);
            tvRVScanRow = itemView.findViewById(R.id.tvRVScanRow);
            tvRVScanBarcode = itemView.findViewById(R.id.tvRVScanBarcode);
            tvRVScanLineNo = itemView.findViewById(R.id.tvRVScanLineNo);

            tvRVScanItemQty = itemView.findViewById(R.id.tvRVScanItemQty);
            btnScanEdit = itemView.findViewById(R.id.btnScanEdit);
            btnScanDelete = itemView.findViewById(R.id.btnScanDelete);
        }
    }
}
