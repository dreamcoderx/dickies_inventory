package com.btw.dickies.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.btw.dickies.sql.DatabaseHelper;
import com.btw.dickies.R;
import java.util.List;

public class recycleradapterview extends RecyclerView.Adapter<recycleradapterview.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context context;
    //private ArrayList<Contacts> listContacts;
    //private ArrayList<Contacts> mArrayList;

    private DatabaseHelper mDatabase;

    // data is passed into the constructor
    public recycleradapterview(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        this.context = context;
        //this.listContacts = listContacts;
        //this.mArrayList=listContacts;
       // mDatabase = new SqliteDatabase(context);
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.tvuserid.setText(animal);
        holder.tvusername.setText(animal);
        holder.tvemail.setText(animal);

        //holder.txtuserid.setText(animal);
        //holder.myTextView.setText(animal);
        //holder.
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvuserid, tvusername, tvemail;

        ViewHolder(View itemView) {
            super(itemView);
            tvuserid = itemView.findViewById(R.id.tvuserid);
            tvusername= itemView.findViewById(R.id.tvusername);
            tvemail= itemView.findViewById(R.id.tvemail);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
