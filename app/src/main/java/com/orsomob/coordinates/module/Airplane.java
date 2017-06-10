package com.orsomob.coordinates.module;

import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.data.module.AirplaneData_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 5/28/17.
 */


public class Airplane implements Serializable {

    public static String sAirplane = "Airplane";

    private Integer mId;

    private String mName = sAirplane;

    private Float mDirection;

    private Float mSpeed;

    private Float mCoordinateX;

    private Float mCoordinateY;

    private Float mRadius;

    private Float mDegree;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer aId) {
        mId = aId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public Float getDirection() {
        return mDirection;
    }

    public void setDirection(Float aDirection) {
        mDirection = aDirection;
    }

    public Float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Float aSpeed) {
        mSpeed = aSpeed;
    }

    public Float getCoordinateX() {
        return mCoordinateX;
    }

    public void setCoordinateX(Float aCoordinateX) {
        mCoordinateX = aCoordinateX;
    }

    public Float getCoordinateY() {
        return mCoordinateY;
    }

    public void setCoordinateY(Float aCoordinateY) {
        mCoordinateY = aCoordinateY;
    }

    public Float getRadius() {
        return mRadius;
    }

    public void setRadius(Float aRadius) {
        mRadius = aRadius;
    }

    public Float getDegree() {
        return mDegree;
    }

    public void setDegree(Float aDegree) {
        mDegree = aDegree;
    }

    public static AirplaneData loadFromDataBase(Airplane aAirplane) {
        return SQLite.select().from(AirplaneData.class).where(AirplaneData_Table.id.is(aAirplane.getId())).querySingle();
    }

    public static AirplaneData saveToDatabase(Airplane aAirplane) {
        AirplaneData lAirplaneData = new AirplaneData();

        lAirplaneData.setName(aAirplane.getName());
        lAirplaneData.setCoordinateX(aAirplane.getCoordinateX().doubleValue());
        lAirplaneData.setCoordinateY(aAirplane.getCoordinateY().doubleValue());
        lAirplaneData.setSpeed(aAirplane.getSpeed().doubleValue());
        lAirplaneData.setDirection(aAirplane.getDirection().doubleValue());
        lAirplaneData.setDegree(aAirplane.getDegree().doubleValue());
        lAirplaneData.setRadius(aAirplane.getRadius().doubleValue());

        lAirplaneData.save();

        return lAirplaneData;
    }

    public static Airplane loadFromDatabase(AirplaneData aAirplaneData) {
        Airplane lAirplane = new Airplane();

        lAirplane.setId(aAirplaneData.getId());
        lAirplane.setName(aAirplaneData.getName());
        lAirplane.setCoordinateX(aAirplaneData.getCoordinateX().floatValue());
        lAirplane.setCoordinateY(aAirplaneData.getCoordinateY().floatValue());
        lAirplane.setSpeed(aAirplaneData.getSpeed().floatValue());
        lAirplane.setDirection(aAirplaneData.getDirection().floatValue());
        lAirplane.setDegree(aAirplaneData.getDegree().floatValue());
        lAirplane.setRadius(aAirplaneData.getRadius().floatValue());

        return lAirplane;
    }

    public static List<Airplane> loadListFromDataBase(List<AirplaneData> aAirplaneDatas) {
        List<Airplane> lAirplanes = new ArrayList<>();
        for (AirplaneData lAirplaneData : aAirplaneDatas) {
            lAirplanes.add(loadFromDatabase(lAirplaneData));
        }
        return lAirplanes;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDirection=" + mDirection +
                ", mSpeed=" + mSpeed +
                ", mCoordinateX=" + mCoordinateX +
                ", mCoordinateY=" + mCoordinateY +
                '}';
    }
}
