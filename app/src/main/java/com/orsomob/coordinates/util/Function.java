package com.orsomob.coordinates.util;

import android.graphics.Point;

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

}
