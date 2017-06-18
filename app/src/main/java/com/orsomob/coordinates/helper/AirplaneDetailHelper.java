package com.orsomob.coordinates.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;

/**
 * Created by LucasOrso on 6/17/17.
 */

public class AirplaneDetailHelper {

    private LinearLayout mLinearLayout;
    private Airplane mAirplane;
    private ImageView mImageView;
    private TextView mTextViewId;
    private TextView mTextViewName;
    private TextView mTextViewSpeed;
    private TextView mTextViewDirection;
    private TextView mTextViewX;
    private TextView mTextViewY;
    private TextView mTextViewDegrees;
    private TextView mTextViewRadius;
    private View mView;
    private boolean isShowing;

    public AirplaneDetailHelper(View aView) {
        mView = aView;
        init();
    }

    private void init() {
        getReferences();
    }

    private void getReferences() {
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.include_detail);
        mImageView = (ImageView) mLinearLayout.findViewById(R.id.iv_airplane);
        mTextViewId = (TextView) mLinearLayout.findViewById(R.id.tx_id);
        mTextViewName = (TextView) mLinearLayout.findViewById(R.id.tx_name);
        mTextViewSpeed = (TextView) mLinearLayout.findViewById(R.id.tx_speed);
        mTextViewDirection = (TextView) mLinearLayout.findViewById(R.id.tx_direction);
        mTextViewX = (TextView) mLinearLayout.findViewById(R.id.tx_x);
        mTextViewY = (TextView) mLinearLayout.findViewById(R.id.tx_y);
        mTextViewDegrees = (TextView) mLinearLayout.findViewById(R.id.tx_degree);
        mTextViewRadius = (TextView) mLinearLayout.findViewById(R.id.tx_radius);
    }

    private void setValues() {
        mImageView.setRotation(mAirplane.getDirection());
        mTextViewId.setText(String.valueOf(mAirplane.getId()));
        mTextViewName.setText(mAirplane.getName());
        mTextViewSpeed.setText(String.valueOf(mAirplane.getSpeed()));
        mTextViewDirection.setText(String.valueOf(mAirplane.getDirection()));
        mTextViewX.setText("X " + String.valueOf(mAirplane.getCoordinateX()));
        mTextViewY.setText("Y " + String.valueOf(mAirplane.getCoordinateY()));
        mTextViewDegrees.setText(String.valueOf(mAirplane.getDegree()) + "º");
        mTextViewRadius.setText("R." + String.valueOf(mAirplane.getRadius()));
    }

    public View getLayout() {
        return mLinearLayout;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean aShowing) {
        isShowing = aShowing;
    }

    public void setAiplane(Airplane aAirplane) {
        mAirplane = aAirplane;
        setValues();
    }
}
