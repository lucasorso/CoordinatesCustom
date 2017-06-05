package com.orsomob.coordinates.data.module;

import com.orsomob.coordinates.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by LucasOrso on 6/4/17.
 */

@Table(database = AppDatabase.class)
public class AirplaneData extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private Double direction;

    @Column
    private Double speed;

    @Column
    private Double coordinateX;

    @Column
    private Double coordinateY;

    @Column
    private Double radius;

    @Column
    private Double degree;

    public Integer getId() {
        return id;
    }

    public void setId(Integer aId) {
        id = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double aDirection) {
        direction = aDirection;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double aSpeed) {
        speed = aSpeed;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(Double aCoordinateX) {
        coordinateX = aCoordinateX;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(Double aCoordinateY) {
        coordinateY = aCoordinateY;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double aRadius) {
        radius = aRadius;
    }

    public Double getDegree() {
        return degree;
    }

    public void setDegree(Double aDegree) {
        degree = aDegree;
    }
}
