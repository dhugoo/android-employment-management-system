package com.example.project5;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    private Context context;
   private Activity activity;
    private ArrayList as;

    CustomAdapter(Activity activity, Context context, ArrayList as)
    {
        this.activity=activity;
        this.context=context;
        this.as=as;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        data te= (data) as.get(position);
        holder.a.setText((""+te.getD_id()));
        holder.b.setText((""+te.getD_name()));
        holder.c.setText((""+te.getD_sex()));
        holder.d.setText((""+te.getD_salary()));
        holder.e.setText((""+te.getD_sales()));
        holder.f.setText((""+te.getD_rate()));

    }

    @Override
    public int getItemCount() {
        return as.size();
    }

    public void delete(int position){
        as.remove(position);
        notifyItemRemoved(position);
    }

    public void clearRV()
    {

        for (int i = 0; i < this.getItemCount(); i++) {
            this.delete(i);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView a,b,c,d,e,f;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            a=itemView.findViewById(R.id.idEm);
            b=itemView.findViewById(R.id.nameEm);
            c=itemView.findViewById(R.id.sexEm);
            d=itemView.findViewById(R.id.bsEm);
            e=itemView.findViewById(R.id.tsEm);
            f=itemView.findViewById(R.id.crEm);
        }
    }

}
