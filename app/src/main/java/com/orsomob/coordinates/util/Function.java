package com.orsomob.coordinates.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.PointDouble;

import static java.lang.Integer.parseInt;

/**
 * Created by LucasOrso on 6/2/17.
 */

public class Function {

    private static final String TAG = "FUNCTION";

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
     * @param aStrinNumber string to format
     * @return Integer
     */
    public static Integer convertStringToInteger(String aStrinNumber) {
        Integer lInteger = null;
        try {
            lInteger = parseInt(aStrinNumber);
        } catch (NumberFormatException aE) {
            Log.e(TAG, "convertStringToInteger: " + aE.getMessage());
            aE.printStackTrace();
            Double lDouble;
            if (aStrinNumber != null && !aStrinNumber.isEmpty()) {
                lDouble = Double.valueOf(aStrinNumber);
                lInteger = lDouble.intValue();
            }
        }
        return lInteger;
    }

    /**
     * @param aStrinNumber string to format
     * @return Float
     */
    public static Float convertStringToFloat(String aStrinNumber) {
        Float lFloat;
        try {
            lFloat = Float.valueOf(aStrinNumber);
        } catch (NumberFormatException aE) {
            Log.e(TAG, "convertStringToFloat: " + aE.getMessage());
            aE.printStackTrace();
            return null;
        }
        return lFloat;
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

    /**
     * @param aPointOfRotate the point that to rotate
     * @param aAtualPoint    the atual point, tha want
     * @param angle          angle to rotate
     * @return Point with new coordinates
     */
    public static PointDouble rotationPoint(PointDouble aPointOfRotate, PointDouble aAtualPoint, Double angle) {
        PointDouble lNewPoint = new PointDouble();

        Log.i(TAG, "rotationPoint -aPointOfRotate- Point X:  " + aPointOfRotate.getX());
        Log.i(TAG, "rotationPoint -aPointOfRotate- Point Y:  " + aPointOfRotate.getY());

        Log.i(TAG, "rotationPoint -aAtualPoint- Point X:  " + aAtualPoint.getX());
        Log.i(TAG, "rotationPoint -aAtualPoint- Point Y:  " + aAtualPoint.getY());

        try {
            double x1 = aAtualPoint.getX() - aPointOfRotate.getX();
            double y1 = aAtualPoint.getY() - aPointOfRotate.getY();

            double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
            double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

            Double x = x2 + aPointOfRotate.getX();
            Double y = y2 + aPointOfRotate.getY();

            lNewPoint.set(x, y);

            Log.i(TAG, "rotationPoint -NewPoint- Point X:  " + lNewPoint.getX());
            Log.i(TAG, "rotationPoint -NewPoint- Point Y:  " + lNewPoint.getY());

            return lNewPoint;
        } catch (Exception aE) {
            aE.printStackTrace();
            Log.e(TAG, "rotationPoint -Exception- ERROR:  " + aE.getMessage());
            return null;
        }
    }
}
