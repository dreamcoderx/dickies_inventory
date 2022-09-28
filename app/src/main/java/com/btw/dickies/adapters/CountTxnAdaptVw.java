package com.btw.dickies.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.btw.dickies.R;
import com.btw.dickies.model.ScanTxn;
import java.util.ArrayList;

public class CountTxnAdaptVw extends RecyclerView.Adapter<CountTxnAdaptVw.viewHolder>{
    Context context;
    AppCompatActivity activity;
    ArrayList<ScanTxn> arr;
    public CountTxnAdaptVw(Context context, AppCompatActivity activity, ArrayList<ScanTxn> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arr = arrayList;
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.rv_scan , vg, false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(final viewHolder vh, int pos) {
        vh.barcode.setText( arr.get(pos).getBarcode());
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView barcode;
        public viewHolder(View itemView) {
            super(itemView);
            barcode = itemView.findViewById(R.id.tvScanValue);
        }
    }
}

