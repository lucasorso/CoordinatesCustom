package com.orsomob.coordinates.module;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by LucasOrso on 5/28/17.
 */

@RealmClass
public class Airplane extends RealmObject{

    @PrimaryKey
    @Required
    private Long mId;
    @Required
    private String mName;
    @Required
    private Long mDirection;
    @Required
    private Long mSpeed;
    @Required
    private Long mCoordinateX;
    @Required
    private Long mCoordinateY;

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

    public Long getDirection() {
        return mDirection;
    }

    public void setDirection(Long aDirection) {
        mDirection = aDirection;
    }

    public Long getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Long aSpeed) {
        mSpeed = aSpeed;
    }

    public Long getCoordinateX() {
        return mCoordinateX;
    }

    public void setCoordinateX(Long aCoordinateX) {
        mCoordinateX = aCoordinateX;
    }

    public Long getCoordinateY() {
        return mCoordinateY;
    }

    public void setCoordinateY(Long aCoordinateY) {
        mCoordinateY = aCoordinateY;
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
