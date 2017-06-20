package com.orsomob.coordinates.module;

import com.orsomob.coordinates.db.AirplaneDB;
import com.orsomob.coordinates.db.AirplaneDB_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 5/28/17.
 */


public class Airplane implements Serializable {

    private Integer mId;

    private String mName;

    private Float mDirection;

    private Float mSpeed;

    private Float mCoordinateX;

    private Float mCoordinateY;

    private Float mRadius;

    private Float mTheta;

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

    public Float getTheta() {
        return mTheta;
    }

    public void setTheta(Float aTheta) {
        mTheta = aTheta;
    }

    public static AirplaneDB loadFromDataBase(Airplane aAirplane) {
        return SQLite.select().from(AirplaneDB.class).where(AirplaneDB_Table.id.is(aAirplane.getId())).querySingle();
    }

    public static Airplane loadAirplaneFromDataBase(Airplane aAirplane) {
        AirplaneDB lAirplaneDB = SQLite.select().from(AirplaneDB.class).where(AirplaneDB_Table.id.is(aAirplane.getId())).querySingle();
        return loadFromDatabase(lAirplaneDB);
    }

    public static AirplaneDB saveToDatabase(Airplane aAirplane) {
        AirplaneDB lAirplaneDB = new AirplaneDB();

        lAirplaneDB.setName(aAirplane.getName());
        lAirplaneDB.setCoordinateX(aAirplane.getCoordinateX().doubleValue());
        lAirplaneDB.setCoordinateY(aAirplane.getCoordinateY().doubleValue());
        lAirplaneDB.setSpeed(aAirplane.getSpeed().doubleValue());
        lAirplaneDB.setDirection(aAirplane.getDirection().doubleValue());
        lAirplaneDB.setTheta(aAirplane.getTheta().doubleValue());
        lAirplaneDB.setRadius(aAirplane.getRadius().doubleValue());

        lAirplaneDB.save();

        return lAirplaneDB;
    }

    public static Airplane loadFromDatabase(AirplaneDB aAirplaneDB) {
        Airplane lAirplane = new Airplane();

        lAirplane.setId(aAirplaneDB.getId());
        lAirplane.setName(aAirplaneDB.getName());
        lAirplane.setCoordinateX(aAirplaneDB.getCoordinateX().floatValue());
        lAirplane.setCoordinateY(aAirplaneDB.getCoordinateY().floatValue());
        lAirplane.setSpeed(aAirplaneDB.getSpeed().floatValue());
        lAirplane.setDirection(aAirplaneDB.getDirection().floatValue());
        lAirplane.setTheta(aAirplaneDB.getTheta().floatValue());
        lAirplane.setRadius(aAirplaneDB.getRadius().floatValue());

        return lAirplane;
    }

    public static List<Airplane> loadListFromDataBase(List<AirplaneDB> aAirplaneDBs) {
        List<Airplane> lAirplanes = new ArrayList<>();
        for (AirplaneDB lAirplaneDB : aAirplaneDBs) {
            lAirplanes.add(loadFromDatabase(lAirplaneDB));
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
