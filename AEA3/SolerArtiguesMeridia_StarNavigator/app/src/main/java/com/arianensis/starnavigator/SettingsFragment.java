package com.arianensis.starnavigator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Logout button and listener
        Button logout_button = settingsView.findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This deletes the sharedPreferences of the logged username and goes back to the login screen
                Meridia.logout();

                Intent askLogin = new Intent(settingsView.getContext(), LoginActivity.class);
                startActivity(askLogin);
            }
        });

        // Declare the language buttons. It is a radio meaning that one and only one must be checked
        RadioGroup languageSelector = settingsView.findViewById(R.id.language_selector);
        RadioButton lang0 = settingsView.findViewById(R.id.lang0);
        // if and only if the language is English, the first button will be checked by default
        lang0.setChecked(Meridia.getLanguage().equals("en-us"));
        RadioButton lang1 = settingsView.findViewById(R.id.lang1);
        // if and only if the language is Catalan, the second button will be checked by default
        lang1.setChecked(Meridia.getLanguage().equals("ca"));
        RadioButton lang2 = settingsView.findViewById(R.id.lang2);
        // if and only if the language is Spanish, the third button will be checked by default
        lang2.setChecked(Meridia.getLanguage().equals("es"));

        // When the radio changes (a button other than the default one) is clicked, change the language and reload
        languageSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (languageSelector.getCheckedRadioButtonId() == R.id.lang0) Meridia.setLanguage("en-us", settingsView.getContext());
                if (languageSelector.getCheckedRadioButtonId() == R.id.lang1) Meridia.setLanguage("ca", settingsView.getContext());
                if (languageSelector.getCheckedRadioButtonId() == R.id.lang2) Meridia.setLanguage("es", settingsView.getContext());

                // "Jump" to the home screen and restart the activity (otherwise some Strings are not changed)
                Intent back = new Intent(settingsView.getContext(), MainActivity.class);
                startActivity(back);
            }
        });

        // create a random number to show the decimal precision
        double testNumber = new Random().nextDouble()*100;
        TextView decimals = settingsView.findViewById(R.id.decimal_example);
        decimals.setText(Meridia.toDecimalString(testNumber, Meridia.getDecimalPrecision()));

        // create a bar to change the decimal precision
        SeekBar decimalSetting = settingsView.findViewById(R.id.decimal_setting);

        // the decimal precision goes from 1 to 6 but the bar progress starts at 0, so there is a offset of 1
        decimalSetting.setProgress(Meridia.getDecimalPrecision()-1);
        decimalSetting.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener() {
            @Override  // when the bar is moved:
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // change the precision and update the test number to reflect it
                Meridia.setDecimalPrecision(decimalSetting.getProgress()+1);
                decimals.setText(Meridia.toDecimalString(testNumber, Meridia.getDecimalPrecision()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Clear preferences button and listener
        Button clear_button = settingsView.findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.confirm_delete_title);
                builder.setMessage(R.string.confirm_delete_settings)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Meridia.deleteAllPreferences();
                                MainActivity.dbHelper.erase(MainActivity.db);
                                Meridia.logout();
                                System.exit(0);
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

        return settingsView;
    }
}