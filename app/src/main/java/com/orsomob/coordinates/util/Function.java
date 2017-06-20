package com.orsomob.coordinates.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;

import static java.lang.Integer.parseInt;

/**
 * Created by LucasOrso on 6/2/17.
 */

public class Function {

    private static final String TAG = "FUNCTION";
    /*

    Reference from  http://www.engineeringtoolbox.com/converting-cartesian-polar-coordinates-d_1347.html
    Other sites convert to wrong value, but this is correct !

    function calculatePolar(){
        var x = $F('x');
        var y = $F('y');
        var r = Math.pow((Math.pow(x,2) + Math.pow(y,2)),0.5);
        var theta = Math.atan(y/x)*360/2/Math.PI;
        if (x >= 0 && y >= 0) {
            theta = theta;
        } else if (x < 0 && y >= 0) {
            theta = 180 + theta;
        } else if (x < 0 && y < 0) {
            theta = 180 + theta;
        } else if (x > 0 && y < 0) {
            theta = 360 + theta;
        }

        //Box.ajaxReq('/docs/scripts/conv.js',false);
        var msg = ""
            + "Polar Coordinates (" + Conv.rounding(r) + ", " + Conv.rounding(theta) + ")\n"
            + "";
        alert(msg);
     }

     function calculateCartesian(){
        var r = $F('r');
        var theta = $F('theta');
        var x = r * Math.cos(theta * 2 * Math.PI / 360);
        var y = r * Math.sin(theta * 2 * Math.PI / 360);
        //Box.ajaxReq('/docs/scripts/conv.js',false);
        var msg = ""
            + "Cartesian Coordinates (" + Conv.rounding(x) + ", " + Conv.rounding(y) + ")\n"
            + "";
        alert(msg);
     }
    */

    /**
     * @param aCoordX
     * @param aCoordY
     * @return Point
     */
    public static PointDouble convertCartesianToPolar(Float aCoordX, Float aCoordY) {
        PointDouble lPoint = new PointDouble();
        Double lRadius;
        Double lTheta;
        lRadius = Math.pow((Math.pow(aCoordX, 2) + Math.pow(aCoordY, 2)), 0.5);
        lTheta = Math.atan(aCoordY / aCoordX) * 360 / 2 / Math.PI;

        if (aCoordX >= 0 && aCoordY >= 0) {
            lTheta = lTheta;
        } else if (aCoordX < 0 && aCoordY >= 0) {
            lTheta = 180 + lTheta;
        } else if (aCoordX < 0 && aCoordY < 0) {
            lTheta = 180 + lTheta;
        } else if (aCoordX > 0 && aCoordY < 0) {
            lTheta = 360 + lTheta;
        }
        lPoint.set(lRadius, lTheta);
        return lPoint;
    }

    /**
     * @param aRadius
     * @param aTheta
     * @return Point
     */
    public static PointDouble convertPolarToCartesian(Double aRadius, Double aTheta) {
        PointDouble lPoint = new PointDouble();
        Double lCoordinateX = (aRadius * Math.cos(aTheta * (Math.PI / 180)));
        Double lCoordinateY = (aRadius * Math.sin(aTheta * (Math.PI / 180)));
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

        Double radians = (Math.PI / 180) * angle;

        PointDouble lNewPoint = new PointDouble();

        Log.i(TAG, "rotationPoint -aPointOfRotate- Point X:  " + aPointOfRotate.getX());
        Log.i(TAG, "rotationPoint -aPointOfRotate- Point Y:  " + aPointOfRotate.getY());

        Log.i(TAG, "rotationPoint -aAtualPoint- Point X:  " + aAtualPoint.getX());
        Log.i(TAG, "rotationPoint -aAtualPoint- Point Y:  " + aAtualPoint.getY());

        try {
            double x1 = aAtualPoint.getX() - aPointOfRotate.getX();
            double y1 = aAtualPoint.getY() - aPointOfRotate.getY();

            double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

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

    /**
     * @param aAirplane to get coordinates
     * @param aPercent  string to convert to percent and calculate a translate
     * @return PointDouble with new values
     */
    public static PointDouble translatePoint(Airplane aAirplane, String aPercent) {
        PointDouble lPointDouble = new PointDouble();

        Float lfloatValue = convertStringToFloat(aPercent);

        if (lfloatValue != null) {
            Float lPercent = lfloatValue / 100;

            Float lCoordX = aAirplane.getCoordinateX() + (aAirplane.getCoordinateX() * lPercent);
            Float lCoordY = aAirplane.getCoordinateX() + (aAirplane.getCoordinateY() * lPercent);

            lPointDouble.set(lCoordX.doubleValue(), lCoordY.doubleValue());

            return lPointDouble;
        }
        return null;
    }
}
