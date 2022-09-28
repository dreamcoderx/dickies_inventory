package com.btw.dickies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.btw.dickies.R;
import com.btw.dickies.model.Scan;
import com.btw.dickies.model.Search;
import com.btw.dickies.sql.ScanTxnDb;
import com.btw.dickies.utils.singleToneClass;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {

    int custom_list_item;
    private Context mCtx;
    private List<Scan> scanList;
    private Search search;

    public ScanAdapter(Context mCtx, int custom_list_item, List<Scan> scanList, Search search) {
        this.mCtx = mCtx;
        this.custom_list_item = custom_list_item;
        this.scanList = scanList;
        this.search = search;
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_list_item, null);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.ScanViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //getting the product of the specified position
        final Scan scan = scanList.get(position);
        holder.tvRVScanID.setText("SCAN ID: " + String.valueOf(scan.getScanID()));
        holder.tvRVScanRack.setText("RACK: " + scan.getRack());
        holder.tvRVScanRow.setText("ROW: " + scan.getRowScan());
        holder.tvRVScanBarcode.setText("BARCODE: " + scan.getBarcode());
        holder.tvRVScanLineNo.setText("LINE NO: " + String.valueOf(scan.getLineNo()));
        holder.tvRVScanItemQty.setText("QTY: " + String.valueOf(scan.getQty()));
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
                            sdb.updateDelete(scan);
                            Toast.makeText(mCtx, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                            scanList.remove(position);
                            reloadFromDB();
                            notifyDataSetChanged();
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
        EditText etRack, etRow, etQuantity, etBarcode;
        etRack = view.findViewById(R.id.etUpdateRack);
        etRow = view.findViewById(R.id.etUpdateRow);
        etBarcode = view.findViewById(R.id.etUpdateBarcode);
        etQuantity = view.findViewById(R.id.etUpdateQty);
        etRack.setText(scan.getRack());
        etRow.setText(scan.getRowScan());
        etBarcode.setText(scan.getBarcode());
        etQuantity.setText(String.valueOf(scan.getQty()));
        String origRack = scan.getRack();
        final AlertDialog dialog = builder.create();
        dialog.show();
        // CREATE METHOD FOR EDIT THE FORM
        view.findViewById(R.id.btnUpdateQty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rack, row, qty, barcode;
                rack = etRack.getText().toString().trim();
                row = etRow.getText().toString().trim();
                qty = etQuantity.getText().toString().trim();
                barcode = etBarcode.getText().toString().trim();
                if (rack.isEmpty()) {
                    etRack.setError("rack can't be blank!");
                    etRack.requestFocus();
                    return;
                }
                if (row.isEmpty()) {
                    etRow.setError("row can't be blank!");
                    etRow.requestFocus();
                    return;
                }
                if (qty.isEmpty()) {
                    etQuantity.setError("qty can't be blank!");
                    etQuantity.requestFocus();
                    return;
                }
                if (barcode.isEmpty()) {
                    etBarcode.setError("barcode can't be blank!");
                    etBarcode.requestFocus();
                    return;
                }
                singleToneClass sc = com.btw.dickies.utils.singleToneClass.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(System.currentTimeMillis());
                scan.setDateTime(date);
                scan.setScanBy(sc.getUserName());
                ScanTxnDb sdb = new ScanTxnDb(mCtx);
                //rack, row, barcode, qty
                scan.setRack(rack);
                scan.setRowScan(row);
                scan.setBarcode(barcode);
                int mQty = Integer.parseInt(qty);
                scan.setQty(mQty);
                int line_no = 1;
                if (!(origRack.equals(rack))){
                     line_no = sdb.getMaxLineNo(rack)+1;
                }
                scan.setLineNo(line_no);
                //if (sdb.updateLineNoSlip(scan)){
                    if (sdb.updateScan(scan)){
                        Toast.makeText(mCtx, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                //}
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
    private void reloadFromDB() {
        ScanTxnDb s = new ScanTxnDb(mCtx);
        scanList.clear();
        scanList = new ArrayList<>(s.getScanStatic(search));
        notifyDataSetChanged();
    }
}
