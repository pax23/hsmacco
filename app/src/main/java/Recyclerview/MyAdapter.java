package Recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import www.mhsacco.co.zw.R;

public  class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<ListItem> listItems;
    private  Context context;


    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem =listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        holder.textViewAmount.setText(listItem.getAmount());
        holder.textViewDate.setText(listItem.getDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder {

        public TextView textViewHead ;
        public  TextView textViewDesc;
        public TextView textViewAmount;
        public TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHead = itemView.findViewById(R.id.textViewHead);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}