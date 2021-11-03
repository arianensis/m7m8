package com.arianensis.starnavigator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    TextView qTotal, qCustom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        TextView timeAwareWelcome = homeView.findViewById(R.id.home_title1);
        TextView helloUser = homeView.findViewById(R.id.home_username);

        // Set the welcome text according to the current time;
        int hour = Calendar.getInstance().getTime().getHours();
        if (hour > 4 && hour <= 12) timeAwareWelcome.setText(R.string.title_welcome_morning);
        else if (hour > 12 && hour <= 18) timeAwareWelcome.setText(R.string.title_welcome_afternoon);
        else timeAwareWelcome.setText(R.string.title_welcome_evening);

        // Greet the current user
        helloUser.setText(SessionManager.loggedUsername + "!");

        // Count total and user-made stars
        qTotal = homeView.findViewById(R.id.total_stars_q);
        qCustom = homeView.findViewById(R.id.custom_stars_q);
        recountStars();

        // Button to delete the stars that the user has created, with dialog confirmation
        Button btn_delete_userdata = homeView.findViewById(R.id.delete_user);
        btn_delete_userdata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.confirm_delete);
                builder.setMessage("Going to delete " + qCustom.getText() + " stars")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.dbHelper.eraseCustom(MainActivity.db);
                                Toast.makeText(homeView.getContext(), R.string.delete_custom_message, Toast.LENGTH_SHORT).show();
                                recountStars();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //KO METHOD
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Button to delete all the stars, with dialog confirmation
        Button btn_delete_alldata = homeView.findViewById(R.id.delete_all);
        btn_delete_alldata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.confirm_delete);
                builder.setMessage("Going to delete " + qTotal.getText() + " stars")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.dbHelper.erase(MainActivity.db);
                                Toast.makeText(homeView.getContext(), R.string.delete_all_message, Toast.LENGTH_SHORT).show();
                                recountStars();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Button to restore default stars, with toast confirmation
        Button restore = homeView.findViewById(R.id.btn_restore_data);
        restore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SessionManager.loadDefaultStars();
                Toast.makeText(homeView.getContext(), R.string.restore_message, Toast.LENGTH_SHORT).show();
                recountStars();
            }
        });

        return homeView;
    }

    public void recountStars()
    {
        qTotal.setText(""+MainActivity.dbHelper.listStars(MainActivity.db).size());
        qCustom.setText(""+MainActivity.dbHelper.listStars(MainActivity.db, " where is_custom='true'").size());
    }
}