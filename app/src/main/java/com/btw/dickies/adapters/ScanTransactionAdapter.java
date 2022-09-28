package com.btw.dickies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.dickies.model.Scan;
import com.btw.dickies.R;
import com.btw.dickies.sql.ScanTxnDb;

import java.util.ArrayList;
import java.util.List;

public class ScanTransactionAdapter
        extends RecyclerView.Adapter<ScanTransactionAdapter.viewHolder>{

    Context context;
    AppCompatActivity activity;
    List<Scan> listScan;
    //ModelDbh modelDH;

    public ScanTransactionAdapter(Context context, AppCompatActivity activity, List<Scan> listScan) {
        this.context = context;
        this.activity  = activity ;
        this.listScan = listScan;
    }

    @Override
    public ScanTransactionAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View view = LayoutInflater.from(context).
        //        inflate(R.layout.recview_row_scan_transaction, viewGroup, false);
        //return new viewHolder(view);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_row_scan_transaction, null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScanTransactionAdapter.viewHolder vh, @SuppressLint("RecyclerView") final int position) {
        try{
        final Scan scan = listScan.get(position);
        vh.tvScanID.setText(String.valueOf(scan.getScanID()));
        vh.tvRack.setText(scan.getRack());
        vh.tvRow.setText(scan.getRowScan());
        vh.tvBarcode.setText(scan.getBarcode());
        vh.tvLineNo.setText(String.valueOf(scan.getLineNo()));
        vh.tvQty.setText(String.valueOf(scan.getQty()));
        vh.tvScanBy.setText(scan.getScanBy());
        vh.tvScanDateTime.setText(scan.getDateTime());
        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listScan.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        //TextView id;
        //ImageView buttonDelete;

        TextView tvScanID, tvRack, tvRow;
        TextView tvBarcode, tvLineNo, tvQty;
        TextView tvScanBy, tvScanDateTime;
        ImageView btnEdit, btnDelete;
        public viewHolder(View itemView) {
            super(itemView);
            //id = itemView.findViewById(R.id.tvScanValue);
            //buttonDelete = itemView.findViewById(R.id.imgDelete);
            tvScanID = itemView.findViewById(R.id.tvRvScanId);
            tvBarcode = itemView.findViewById(R.id.tvRvScanBarcode);
            tvRack = itemView.findViewById(R.id.tvRvScanRack);
            tvRow = itemView.findViewById(R.id.tvRvScanRow);
            tvLineNo = itemView.findViewById(R.id.tvRvLineNo);
            tvQty = itemView.findViewById(R.id.tvRVScanItemQty);
            tvScanDateTime = itemView.findViewById(R.id.tvRvScanDateTime);
            tvScanBy = itemView.findViewById(R.id.tvRvScanBy);
            btnEdit = itemView.findViewById(R.id.btnEditScan);
            btnDelete = itemView.findViewById(R.id.btnDeleteScan);
        }
    }


}
