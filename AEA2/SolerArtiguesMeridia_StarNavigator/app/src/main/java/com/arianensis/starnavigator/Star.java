package com.arianensis.starnavigator;

import android.util.Log;

import com.arianensis.starnavigator.DB.StarsDBHelper;

import java.util.ArrayList;

// The class for all objects that will appear in the app
public class Star {
    // single object for the Star "list"
    private static ArrayList<Star> loadedStars = new ArrayList<>();

    // public properties
    public final static char[] SPECTRE_LETTERS = { 'O', 'B', 'A', 'F', 'G', 'K', 'M', 'L'};
    public final static String[] SPECTRE_COLORS = {
            "#66ccff", "#ccffff", "#ffffff", "#ffffcc", "#ffff00", "#ffdb4d", "#ff9933", "#cc6600"
    };

    // per-object properties
    private double[] cubeCoords = new double[3]; // { x, y, x }
    /*  the Cubical Coordinates determine the position of a Star within an orthogonal 3D space
     *  (three distances such as "height", "width" and "depth" whit the Sun being (0,0,0)
     *  With simple trigonometry one can convert between the two types of coordinates (methods below)
     */

    private String name; // the list of different names a star has in the database
    private String spectre; // the spectral class (OBAFGKML from hotter to colder optionally followed by numbers)
    // private String appMagn; // apparent magnitude (how scattered the light from the star reaches Earth. The less the brighter)

    public final boolean isCustom; // false if the star is loaded from official databases, true if it's an user entry

    // CONSTRUCTORS
    public Star(String name, double[] coords, boolean areSpherical, String spectre, boolean isCustom) { // names Array
        // generate a Star object from coordinates
        this.name = name;
        this.spectre = spectre;
        if (areSpherical) //sets the coordinates of the desired type and calculates the other
        {
            // this.spheriCoords = coords;
            coords[0] *= 15;
            this.cubeCoords = sphericalToCubic(coords);
        }
        else
        {
            this.cubeCoords = coords;
            // this.spheriCoords = cubicToSpherical(cubeCoords);
        }

        // add the star we created to the list, unless it already exists
        this.isCustom = isCustom;
        if (!exists(name)){
            loadedStars.add(this);
            MainActivity.dbHelper.insertStar(MainActivity.db, this);
            //Log.i("DIVAC", "Added Star: " + name);
        } //else Log.i("DIVAC", "Error adding star");
    }


    // Recommended constructor to load from Simbad Database (will be useful when using the API, not now)
    /*public Star(String[] allNames, double ra, double dec, double parallax, String spectre) {
        // generate a star from Right ascension, Declination and Parallax (given by the database)
        // this simply calculates the distance from the parallax, creates the 3d vector and calls the other constructor
        this(allNames, new double[]{ra, dec, paraDist(parallax)}, true, spectre, false);
    }*/


    // GETTERS
    public double[] getSpheriCoords() { return Star.cubicToSpherical(this.cubeCoords); }
    public double[] getCubeCoords() { return this.cubeCoords; }
    public String getName() { return this.name; }
    public String getSpectre() { return this.spectre; }
    public double getParallax() { return Star.paraDist(this.getSpheriCoords()[2]); } // in LY
    public String getColor() {
        // take the first character of the spectral type (the letter) and see its position
        int colorIndex = "OBAFGKML".indexOf(this.getSpectre().charAt(0));
        // for objects not in the list (eg Neutron stars) we will use white
        if (colorIndex < 0) return "#FFFFFF";
        return SPECTRE_COLORS[colorIndex];
    }
    public boolean getIsCustom() { return this.isCustom; }

    // CLASS GETTERS
    public static ArrayList<Star> getAllStars() { return loadedStars; }

    // CONVERSION METHODS
    public static double paraDist(double parallaxOrDistance) {
        /*  Calculate parallax from distance or vice versa.
         *  We can use the sin(a) = a for extremely small angles.
         *  for a distance of 1pc, the parallax would be 1 arcsecond = 1000 arcmilliseconds
         *  for a distance of 1000pc = 3262.564LY, the parallax would be 1 arcmillisecond
         *  so we can use the approximation ```3262.564 / parallax = distance```
         *  or ```3261.564 / distance = parallax```
         */
        return 3261.564/parallaxOrDistance;
    }

    public static double[] sphericalToCubic (double[] spheriCoords) {
        // To convert spherical to cubic coordinates we use the distance and the angles
        double distance = spheriCoords[2];
        double ra = Math.toRadians(spheriCoords[0]);
        double dec = Math.toRadians(spheriCoords[1]);
        // first we use right ascension to calculate the horizontal and vertical components
        double horDist = distance * Math.cos(ra);
        // the vertical component is already the Y coordinate
        double Y = distance * Math.sin(ra);
        // finally we use the horizontal component with the declination to calculate the projection on the plane
        double X = horDist * Math.cos(dec);
        double Z = horDist * Math.sin(dec);

        return new double[] {X, Y, Z};
    }

    public static double[] cubicToSpherical (double[] cubeCoords) {
        /*  the Spherical Coordinates determine the position of a Star as seen from the Solar System
         *  They are divided in Right ascension, declination and distance. Distance in LightYears
         *  (two angles and a distance determine a point in space)
         */

        // extract the components for easier calculations
        double X = cubeCoords[0]; double Y = cubeCoords[1]; double Z = cubeCoords[2];
        // To get the spherical coordinates we have to calculate the angles and distance from XYZ
        // first, the distance is just the modulus of the vector. We can get it with Pythagoras theorem
        double dist = Math.sqrt( Math.pow(X,2) + Math.pow(Y,2) + Math.pow(Z,2));
        // the declination is the horizontal component of the angle, so it depends on X and Z and is not affected by Y
        // if the Z is the sinus and the X is the cosinus, Z/X is the tangent, so the angle will be arctan(Z/X)
        double dec = Math.toDegrees(Math.atan(Z/X));

        // the right ascension is the vertical angle, but we need the projection of the vector to calculate it
        // with Pythagoras theorem, we can get the horizontal distance
        double horDist = Math.sqrt( Math.pow(X,2) + Math.pow(Z,2) );
        // and then use this, which is the contiguous cathetus, and the total distance, which is the hypotenuse
        double ra = Math.toDegrees(Math.acos(horDist/dist));

        return new double[] {ra, dec, dist};
    }

    // OTHER CLASS METHODS
    public static Star getStarByName(String nameToSearch) {
        // Searches all the stars in the list
        for (Star star : loadedStars)
        {   // if the name of any star is equal to the argument, return that star
            if (star.getName().equals(nameToSearch)) return star;
        }
        return null; // if not found in all the list
    }
    public static boolean exists(String nameToSearch) { return null!=getStarByName(nameToSearch); }

    // Method to transform the Java object to a set of strings to insert in the database
    public String[] toTableValues() {
        // the array with the values in the same order as they are in the addStar method of the DBHelper
        // NOTE: COORDS IN DATABASE ARE ALWAYS IN SPHERICAL MODE
        String[] values = new String[]{
                ""+getCubeCoords()[0],
                ""+getCubeCoords()[1],
                ""+getCubeCoords()[2],
                getSpectre(),
                ""+getIsCustom()};
        return values;
    }
    // Creates and returns star object from a database entry (inverse function of the above)
    public static Star fromTableValues(String c_name, double c_x, double c_y, double c_z, String c_spectre, Boolean c_is_custom) {
        return new Star(c_name, new double[]{c_x, c_y, c_z}, false, c_spectre, c_is_custom);
    }

    // Deletes all stars in the ArrayList
    public static void deleteAll() { Log.i("VIFOR", ""+loadedStars.size()); loadedStars.clear(); Log.i("ÀFTER", ""+loadedStars.size()); }

    // Deletes all custom stars in the ArrayList
    public static void deleteCustom() {
        for (Star star : loadedStars)
        {
            if (star.getIsCustom()) loadedStars.remove(star);
        }
    }

    // Deletes a given star from the ArrayList
    public static void delete(Star star) { loadedStars.remove(star); }
}
