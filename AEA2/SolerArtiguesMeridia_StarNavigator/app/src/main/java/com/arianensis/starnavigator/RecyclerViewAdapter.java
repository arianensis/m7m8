package com.arianensis.starnavigator;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Star> starsList;

    public RecyclerViewAdapter(ArrayList<Star> arrN){
        starsList = arrN;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.etiquetaNom.setText(starsList.get(position).getName());

        holder.etiquetaNom.setTextColor(Color.parseColor(starsList.get(position).getColor()));
        holder.dibujitu.setTextColor(Color.parseColor(starsList.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return starsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView etiquetaNom, dibujitu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etiquetaNom = itemView.findViewById(R.id.itemName);
            dibujitu = itemView.findViewById(R.id.starSymbol);
        }
    }
}
