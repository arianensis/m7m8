package com.arianensis.starnavigator;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.starName.setText(starsList.get(position).getName());
        holder.starName.setTextColor(Color.parseColor(starsList.get(position).getColor()));
        holder.starSymbol.setTextColor(Color.parseColor(starsList.get(position).getColor()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.button.setBackgroundColor(Color.parseColor(starsList.get(position).getColor()));
                holder.starName.setTextColor(Color.parseColor("#000000"));
                holder.starSymbol.setTextColor(Color.parseColor("#000000"));
                SystemClock.sleep(200);

                AppCompatActivity app = (AppCompatActivity) v.getContext();

                InfoFragment infoFragment = new InfoFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("Star", starsList.get(position));
                infoFragment.setArguments(bundle);

                app.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, infoFragment).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return starsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout button;
        TextView starName, starSymbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            starName = itemView.findViewById(R.id.itemName);
            starSymbol = itemView.findViewById(R.id.starSymbol);
            button = itemView.findViewById(R.id.item);
        }
    }
}
