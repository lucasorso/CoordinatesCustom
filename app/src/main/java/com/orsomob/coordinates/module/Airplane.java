package com.orsomob.coordinates.module;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by LucasOrso on 5/28/17.
 */

@RealmClass
public class Airplane extends RealmObject implements Serializable{

    public static String sAirplane = "Airplane";

    @PrimaryKey
    private Long mId;

    private String mName = "Airplane ";

    @Required
    private Float mDirection;

    @Required
    private Float mSpeed;

    @Required
    private Float mCoordinateX;

    @Required
    private Float mCoordinateY;

    @Required
    private Float mRadius;

    @Required
    private Float mDegree;

    public Long getId() {
        return mId;
    }

    public void setId(Long aId) {
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
