package com.arianensis.starnavigator;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View infoView = inflater.inflate(R.layout.fragment_info, container, false);

        // Get the info from the element that is stored in the bundle
        Star currentStar = (Star) getArguments().getSerializable("Star");

        // Define the views
        //// Basic info
        TextView view_name = infoView.findViewById(R.id.prop_name);
        TextView view_class = infoView.findViewById(R.id.prop_class);
        TextView tag_class0 = infoView.findViewById(R.id.tag_class0);
        TextView tag_class1 = infoView.findViewById(R.id.tag_class1);

        //// Spherical coordinates
        TextView view_ra = infoView.findViewById(R.id.prop_ra);
        TextView view_dec = infoView.findViewById(R.id.prop_dec);
        TextView view_dist = infoView.findViewById(R.id.prop_dist);

        //// Cubic coordinates
        TextView view_x = infoView.findViewById(R.id.prop_x);
        TextView view_y = infoView.findViewById(R.id.prop_y);
        TextView view_z = infoView.findViewById(R.id.prop_z);

        // Write the information from the current object in the views
        view_name.setText(currentStar.getName());
        view_name.setTextColor(Color.parseColor(currentStar.getColor()));

        // The name and class will be written in the color of th star
        tag_class0.setTextColor(Color.parseColor(currentStar.getColor()));
        tag_class1.setTextColor(Color.parseColor(currentStar.getColor()));
        view_class.setText(currentStar.getSpectre());
        view_class.setTextColor(Color.parseColor(currentStar.getColor()));

        // If the distance property is extremely small it means you are viewing the local star (the sun by default)
        if (currentStar.getSpheriCoords()[2] < 0.0000005) {
            // In this case we hide the spherical coordinates and show a message
            infoView.findViewById(R.id.sphericoords_panel).setVisibility(View.INVISIBLE); // hide coords table
            infoView.findViewById(R.id.prop_local).setVisibility(View.VISIBLE); // show message title
            infoView.findViewById(R.id.prop_local_v).setVisibility(View.VISIBLE); // show message body
        } else {
            // if the star is not local, retrieve the spherical coordinates
            view_ra.setText(Meridia.toDecimalString(currentStar.getSpheriCoords()[0], Meridia.getDecimalPrecision()));
            view_dec.setText(Meridia.toDecimalString(currentStar.getSpheriCoords()[1], Meridia.getDecimalPrecision()));
            view_dist.setText(Meridia.toDecimalString(currentStar.getSpheriCoords()[2], Meridia.getDecimalPrecision()));
        }

        // finally retrieve the cubic coordinates
        view_x.setText(Meridia.toDecimalString(currentStar.getCubeCoords()[0],Meridia.getDecimalPrecision()));
        view_y.setText(Meridia.toDecimalString(currentStar.getCubeCoords()[1],Meridia.getDecimalPrecision()));
        view_z.setText(Meridia.toDecimalString(currentStar.getCubeCoords()[2],Meridia.getDecimalPrecision()));

        return infoView;
    }
}