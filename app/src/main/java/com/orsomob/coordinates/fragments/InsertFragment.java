package com.orsomob.coordinates.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.util.Function;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class InsertFragment extends Fragment {

    private View mView;
    private Switch mSwitchTypeCoordinate;
    private LinearLayout mLayoutCartesian;
    private LinearLayout mLayoutPolar;
    private TextInputEditText mEditTextCartesianX;
    private TextInputEditText mEditTextCartesianY;
    private TextInputEditText mEditTextpolarRadius;
    private TextInputEditText mEditTextpolarDegrees;
    private TextInputEditText mEditTextSpeed;
    private TextInputEditText mEditTextDirection;
    private AirplaneListener mAirplaneListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAirplaneListener = (AirplaneListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AirplaneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_insert, container, false);
        init();
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Airplane lAirplane;
                if (validateValues() == 0) {
                    lAirplane = assignValues();
                    sendAirplane(lAirplane);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface AirplaneListener {
        void onReceiveAirplane(Airplane aAirplane);
    }

    public void sendAirplane(Airplane aAirplane) {
        if (mAirplaneListener != null) {
            mAirplaneListener.onReceiveAirplane(aAirplane);
        }
    }

    private void init() {
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getReferences();
        setEvents();
    }

    private void setEvents() {
        mSwitchTypeCoordinate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLayoutCartesian.setVisibility(View.INVISIBLE);
                    mLayoutPolar.setVisibility(View.VISIBLE);
                } else {
                    mLayoutPolar.setVisibility(View.INVISIBLE);
                    mLayoutCartesian.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getReferences() {
        mSwitchTypeCoordinate = (Switch) mView.findViewById(R.id.sw_main);
        mLayoutCartesian = (LinearLayout) mView.findViewById(R.id.ll_cartesian);
        mLayoutPolar = (LinearLayout) mView.findViewById(R.id.ll_polar);
        mEditTextCartesianX = (TextInputEditText) mView.findViewById(R.id.ed_x);
        mEditTextCartesianY = (TextInputEditText) mView.findViewById(R.id.ed_y);
        mEditTextpolarRadius = (TextInputEditText) mView.findViewById(R.id.ed_radius);
        mEditTextpolarDegrees = (TextInputEditText) mView.findViewById(R.id.ed_degrees);
        mEditTextSpeed = (TextInputEditText) mView.findViewById(R.id.ed_speed);
        mEditTextDirection = (TextInputEditText) mView.findViewById(R.id.ed_direction);
    }

    private int validateValues() {
        if (mLayoutCartesian.getVisibility() == View.VISIBLE) {
            String lCartesianX = mEditTextCartesianX.getText().toString();
            if (lCartesianX.isEmpty() && TextUtils.isDigitsOnly(lCartesianX)) {
                mEditTextCartesianX.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextCartesianX.getId();
            }
            String lCartesianY = mEditTextCartesianY.getText().toString();
            if (lCartesianY.isEmpty() && TextUtils.isDigitsOnly(lCartesianY)) {
                mEditTextCartesianY.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextCartesianY.getId();
            }
        } else {
            String lRadius = mEditTextpolarRadius.getText().toString();
            if (lRadius.isEmpty() && TextUtils.isDigitsOnly(lRadius)) {
                mEditTextpolarRadius.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextpolarRadius.getId();
            }
            String lAngle = mEditTextpolarDegrees.getText().toString();
            if (lAngle.isEmpty() && TextUtils.isDigitsOnly(lAngle)) {
                mEditTextpolarDegrees.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextpolarDegrees.getId();
            }
        }
        String lSpeed = mEditTextSpeed.getText().toString();
        if (lSpeed.isEmpty() && TextUtils.isDigitsOnly(lSpeed)) {
            mEditTextSpeed.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextSpeed.getId();
        }
        String lDirection = mEditTextDirection.getText().toString();
        if (lDirection.isEmpty() && TextUtils.isDigitsOnly(lDirection)) {
            mEditTextDirection.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextDirection.getId();
        }
        return 0;
    }

    private Airplane assignValues() {
        Airplane lAirplane = new Airplane();
        Point lPoint;

        float lCoordinateX;
        float lCoordinateY;
        float lRadius;
        float lDegrees;
        float lDirection = Long.valueOf(mEditTextDirection.getText().toString());
        float lSpeed = Float.parseFloat(mEditTextSpeed.getText().toString());


        if (mLayoutCartesian.getVisibility() == View.VISIBLE) {
            lCoordinateX = Float.parseFloat(mEditTextCartesianX.getText().toString());
            lCoordinateY = Float.parseFloat(mEditTextCartesianY.getText().toString());
            lPoint = Function.convertCartesianToPolar(lCoordinateX, lCoordinateY);
            lDegrees = lPoint.x;
            lRadius = lPoint.y;
        } else {
            lDegrees = Float.parseFloat(mEditTextpolarDegrees.getText().toString());
            lRadius = Float.parseFloat(mEditTextpolarRadius.getText().toString());
            lPoint = Function.convertPolarToCartesian(lRadius, lDegrees);
            lCoordinateX = lPoint.x;
            lCoordinateY = lPoint.y;
        }

        lAirplane.setCoordinateX(lCoordinateX);
        lAirplane.setCoordinateY(lCoordinateY);
        lAirplane.setDegree(lDegrees);
        lAirplane.setRadius(lRadius);
        lAirplane.setDirection(lDirection);
        lAirplane.setSpeed(lSpeed);

        return lAirplane;
    }
}
