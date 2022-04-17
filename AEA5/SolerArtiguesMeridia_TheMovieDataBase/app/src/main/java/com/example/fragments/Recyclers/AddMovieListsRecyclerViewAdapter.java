package com.example.fragments.Recyclers;


import static com.example.fragments.Config.DefaultConstants.API_KEY;
import static com.example.fragments.Config.DefaultConstants.SESSION_ID;
import static com.example.fragments.Config.DefaultConstants.retrofit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments.Config.ApiCall;
import com.example.fragments.Config.Meridia;
import com.example.fragments.MainActivity;
import com.example.fragments.Model.List.AddToListRequest;
import com.example.fragments.Model.List.EmptyResponse;
import com.example.fragments.Model.List.TMDBList;
import com.example.fragments.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovieListsRecyclerViewAdapter extends RecyclerView.Adapter<AddMovieListsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<TMDBList> arrayTMDBList;
    private Context context;
    // the recycler view will act differently if we are just seeing the list or adding a movie to it
    private boolean isDetail;
    private Dialog dialog;

    public AddMovieListsRecyclerViewAdapter(ArrayList<TMDBList> arrN, Context c, boolean isDetail, Dialog dialog){
        this.arrayTMDBList = arrN;
        this.context = c;
        this.isDetail = isDetail;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.listTitle.setText(arrayTMDBList.get(i).getName());
        holder.itemCount.setText(String.valueOf(arrayTMDBList.get(i).getItemCount()));

        if (!isDetail) holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete the list '" + arrayTMDBList.get(i).getName() + "' ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Try to delete the list
                        ApiCall apiCall = retrofit.create(ApiCall.class);
                        // call (list_id, api_key, session) to delete list
                        Call<EmptyResponse> call = apiCall.getData(arrayTMDBList.get(i).getId(), API_KEY, SESSION_ID);

                        call.enqueue(new Callback<EmptyResponse>() {
                            @Override
                            public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                                if ((response.code() < 200 || response.code() > 299) && response.code() != 500)
                                    /* any 2xx means success. In this particular operation, the server returns 500 but it still works */ {
                                    Log.i("testApi", "Response code: " + response.code());
                                } else {
                                    Log.i("testApi", "Deleted list '" + arrayTMDBList.get(i).getName() + "'");
                                    dialog.dismiss();
                                    Intent back = new Intent(context, MainActivity.class);
                                    MainActivity.mode = 1;
                                    context.startActivity(back);
                                }
                            }

                            @Override
                            public void onFailure(Call<EmptyResponse> call, Throwable t) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(!isDetail ? new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("testApi", "clicked list " + arrayTMDBList.get(i).getName());
                MainActivity.list = arrayTMDBList.get(i);
                Intent back = new Intent(context, MainActivity.class);
                MainActivity.mode = 2;
                context.startActivity(back);
            }
        }
        : new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("testApi", "adding to list " + arrayTMDBList.get(i).getName());

                ApiCall apiCall = retrofit.create(ApiCall.class);
                // call (list_id, api_key, session, addRequest) to add movie to list
                Call<EmptyResponse> call = apiCall.getData(arrayTMDBList.get(i).getId(), API_KEY, SESSION_ID, new AddToListRequest(Meridia.currentlyViewing.getId()));

                call.enqueue(new Callback<EmptyResponse>() {
                    @Override
                    public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                        if ((response.code() < 200 || response.code() > 299) && response.code()!=403) /* any 2xx means success */ {
                            Log.i("testApi", "Response code: " + response.code());
                        } else {
                            dialog.dismiss();
                            // specifically, if the response code is 403 it means the movie is already in the list
                            Toast.makeText(context,"Movie '" + Meridia.currentlyViewing.getOriginal_title() +
                                    (response.code() == 403 ? "' was already in '" : " added to '")
                                    + arrayTMDBList.get(i).getName() + "'", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EmptyResponse> call, Throwable t) { }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayTMDBList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listTitle;
        TextView itemCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.listTitle);
            itemCount = itemView.findViewById(R.id.itemCount);
        }
    }
}

