package com.orsomob.coordinates.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.orsomob.coordinates.R;

import static com.orsomob.coordinates.fragments.TranslationFragment.TAG;

/**
 * Created by LucasOrso on 6/2/17.
 */

public class Function {

    /**
     * @param aCoordX
     * @param aCoordY
     * @return Point
     */
    public static Point convertCartesianToPolar(Float aCoordX, Float aCoordY) {
        Point lPoint = new Point();
        int lRadius = (int) Math.sqrt((aCoordX * aCoordX) + (aCoordY * aCoordY));
        int lAngleInRadians = (int) Math.acos(aCoordX / lRadius);
        lPoint.set(lRadius, lAngleInRadians);
        return lPoint;
    }

    /**
     * @param aRadius
     * @param aDegrees
     * @return Point
     */
    public static Point convertPolarToCartesian(Float aRadius, Float aDegrees) {
        Point lPoint = new Point();
        int lCoordinateX = (int) (aRadius * Math.cos(aDegrees * (Math.PI / 180)));
        int lCoordinateY = (int) (aRadius * Math.sin(aDegrees * (Math.PI / 180)));
        lPoint.set(lCoordinateX, lCoordinateY);
        return lPoint;
    }

    /**
     * @param aStrinNumber
     * @return Integer
     */
    public static Integer convertStringToInteger(String aStrinNumber) {
        Integer lInteger = null;
        try {
            lInteger = Integer.parseInt(aStrinNumber);
        } catch (NumberFormatException aE) {
            Log.e(TAG, "convertStringToInteger: " + aE.getMessage());
            aE.printStackTrace();
            Double lDouble = Double.valueOf(aStrinNumber);
            lInteger = lDouble.intValue();
        }
        return lInteger;
    }

    /**
     * @param aContext
     * @param aMessage
     */
    public static void showDialoAlert(Context aContext, String aMessage) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(aContext);
        lBuilder.setTitle(R.string.alert).setMessage(aMessage);
        AlertDialog lAlertDialog = lBuilder.create();
        lAlertDialog.show();
    }
}
