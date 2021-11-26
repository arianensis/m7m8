package com.arianensis.starnavigator;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

public class FormFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View formView = inflater.inflate(R.layout.fragment_form, container, false);

        // Add objects for the text fields
        EditText txtName = formView.findViewById(R.id.form_name);
        EditText txtCoord0 = formView.findViewById(R.id.form_coord_0);
        EditText txtCoord1 = formView.findViewById(R.id.form_coord_1);
        EditText txtCoord2 = formView.findViewById(R.id.form_coord_2);
        TextView txtCoordUnit0 = formView.findViewById(R.id.form_coord_unit_0);
        TextView txtCoordUnit1 = formView.findViewById(R.id.form_coord_unit_1);
        TextView spectreValue = formView.findViewById(R.id.value_spectre);
        TextView txtLog = formView.findViewById(R.id.text_log);

        // Add a listener to the button to toggle between types of coordinates
        String[] lastCoords = new String[3];
        ToggleButton coordTypeChange = formView.findViewById(R.id.btn_toggle_coords);
        coordTypeChange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /* save the previous values (just in case the user clicks the button
                 * by accident so they don't lose the values)
                 */
                String[] currentCoords = new String[] {
                        txtCoord0.getText().toString(),
                        txtCoord1.getText().toString(),
                        txtCoord2.getText().toString()
                };

                // erase values


                txtCoord0.setText("");
                txtCoord1.setText("");
                txtCoord2.setText("");
                if (coordTypeChange.isChecked()){ // checked means spherical coords
                    // set the placeholders to ra,dec,dist and the units to degrees,hours,LY
                    txtCoord0.setHint(R.string.coord_ra);
                    txtCoordUnit0.setText("º");
                    txtCoord1.setHint(R.string.coord_dec);
                    txtCoordUnit1.setText("h");
                    txtCoord2.setHint(R.string.coord_dist);
                    // set last field (distance) to be only positive
                    txtCoord2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                } else { // unchecked means cubic coords
                    // set the placeholders to X,Y,Z and the units to LY,LY,LY
                    txtCoord0.setHint("X");
                    txtCoordUnit0.setText(R.string.unit_ly);
                    txtCoord1.setHint("Y");
                    txtCoordUnit1.setText(R.string.unit_ly);
                    txtCoord2.setHint("Z");
                    // set last field (Z coordinate) to accept negative again
                    txtCoord2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            }
        });

        // Add a listener to the bar to determine the spectral class of the star
        SeekBar spectreBar = formView.findViewById(R.id.bar_spectre);
        spectreBar.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int firstDigit = spectreBar.getProgress()/10;
                int secondDigit = spectreBar.getProgress()%10;
                spectreValue.setText("" + Star.SPECTRE_LETTERS[firstDigit] + secondDigit);
                // change the color of the value and the bar
                int starColor = Color.parseColor(Star.SPECTRE_COLORS[firstDigit]);
                spectreValue.setTextColor(starColor);
                spectreBar.getProgressDrawable().setTint(starColor);
                spectreBar.getThumb().setTint(starColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spectreBar.setProgress(42); // Set default spectre to G2 like the Sun

        // Listener for the "Create" button
        Button createStar = formView.findViewById(R.id.btn_submit);
        createStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (txtName.getText().toString().isEmpty() || txtCoord0.getText().toString().isEmpty() ||
                        txtCoord1.getText().toString().isEmpty() || txtCoord2.getText().toString().isEmpty() ) {
                    // if some values are missing, show the "missing values" error
                    txtLog.setText(R.string.create_log_missing);
                } else if (Star.getStarByName(txtName.getText().toString()) != null) {
                    // if we find a star with the same name, show the "repeated" error
                    txtLog.setText(R.string.create_log_repeated);
                } else {
                    // if all parameters are set, get the values and create the star
                    double[] coords = new double[3];
                    coords[0] = Double.parseDouble(txtCoord0.getText().toString());
                    coords[1] = Double.parseDouble(txtCoord1.getText().toString());
                    coords[2] = Double.parseDouble(txtCoord2.getText().toString());
                    String name = txtName.getText().toString();
                    String spClass = spectreValue.getText().toString();
                    boolean coordsAreSpherical = coordTypeChange.isChecked();

                    Star added = new Star (name,coords,coordsAreSpherical,spClass,true);
                    Log.i("INFO", "Added star " + name + " of type " + spClass + " at location ("+
                            added.getCubeCoords()[0]+", "+added.getCubeCoords()[1]+", "+added.getCubeCoords()[2]+")");
                    MainActivity.dbHelper.insertStar(MainActivity.db, added);
                    txtLog.setText(R.string.create_log_correct);
                    // txtLog.setTextColor(Color.parseColor(added.getColor()));
                }
            }
        });

        return formView;
    }
}