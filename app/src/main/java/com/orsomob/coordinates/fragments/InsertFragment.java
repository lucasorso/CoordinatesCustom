package com.orsomob.coordinates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;

import static com.orsomob.coordinates.util.Function.convertCartesianToPolar;
import static com.orsomob.coordinates.util.Function.convertPolarToCartesian;
import static com.orsomob.coordinates.util.Function.convertStringToInteger;
import static com.orsomob.coordinates.util.Function.showDialoAlert;
import static java.lang.Float.parseFloat;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class InsertFragment extends Fragment {

    public static final String TAG = "INSERT_FRAGMENT";

    private View mView;
    private Switch mSwitchTypeCoordinate;
    private LinearLayout mLayoutCartesian;
    private LinearLayout mLayoutPolar;
    private TextInputEditText mEditTextCartesianX;
    private TextInputEditText mEditTextCartesianY;
    private TextInputEditText mEditTextRadius;
    private TextInputEditText mEditTextTheta;
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
                    clearFields();
                } else {
                    Log.e(TAG, "Invalid field");
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
        mEditTextRadius = (TextInputEditText) mView.findViewById(R.id.ed_radius);
        mEditTextTheta = (TextInputEditText) mView.findViewById(R.id.ed_theta);
        mEditTextSpeed = (TextInputEditText) mView.findViewById(R.id.ed_speed);
        mEditTextDirection = (TextInputEditText) mView.findViewById(R.id.ed_direction);
    }

    private int validateValues() {

        String lCartesianX;
        String lCartesianY;
        String lRadius;
        String lTheta;
        String lSpeed;
        String lDirection;

        if (mLayoutCartesian.getVisibility() == View.VISIBLE) {
            lCartesianX = mEditTextCartesianX.getText().toString();
            Integer lCartesianXint = convertStringToInteger(lCartesianX);
            if (lCartesianX.isEmpty() && lCartesianXint == null) {
                mEditTextCartesianX.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextCartesianX.getId();
                } else {
                if (lCartesianXint > 10 || lCartesianXint < -10) {
                    mEditTextCartesianX.setError(getActivity().getString(R.string.invalid_field));
                    showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.max_value_10));
                    return mEditTextCartesianX.getId();
                }
            }
            lCartesianY = mEditTextCartesianY.getText().toString();
            Integer lCartesianYint = convertStringToInteger(lCartesianY);
            if (lCartesianY.isEmpty() && lCartesianYint == null) {
                mEditTextCartesianY.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextCartesianY.getId();
            } else {
                if (lCartesianYint > 10 || lCartesianYint < -10) {
                    mEditTextCartesianY.setError(getActivity().getString(R.string.invalid_field));
                    showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.max_value_10));
                    return mEditTextCartesianX.getId();
                }
            }
        } else {
            /**
             * This means is a polar coordinates has inserted
             */
            lRadius = mEditTextRadius.getText().toString();
            Integer lRadiusInt = convertStringToInteger(lRadius);
            if (lRadius.isEmpty() && lRadiusInt == null) {
                mEditTextRadius.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextRadius.getId();
            }
            lTheta = mEditTextTheta.getText().toString();
            Integer lThetaInt = convertStringToInteger(lTheta);
            if (lTheta.isEmpty() && lThetaInt == null) {
                mEditTextTheta.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextTheta.getId();
            }
            /**
             * Need to be valid to proceed
             */
            PointDouble lPoint = convertPolarToCartesian(lRadiusInt.doubleValue(), lThetaInt.doubleValue());
            if ((lPoint.x > 10 || lPoint.x < -10) ||
                    (lPoint.y > 10 || lPoint.y < -10)) {
                mEditTextTheta.setError(getActivity().getString(R.string.invalid_field));
                mEditTextRadius.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(getActivity(), String.format(getActivity().getResources().getString(R.string.the_polar_coordinate_is_not_valid), lPoint.x, lPoint.y));
                return -1;
            }
        }
        lSpeed = mEditTextSpeed.getText().toString();
        Integer lSpeedInt = convertStringToInteger(lSpeed);
        if (lSpeed.isEmpty() && lSpeedInt == null) {
            mEditTextSpeed.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextSpeed.getId();
        } else {
            if (lSpeedInt <= 0) {
                mEditTextSpeed.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.please_insert_value_greater_than_0));
                return mEditTextSpeed.getId();
            }
        }
        lDirection = mEditTextDirection.getText().toString();
        Integer lDirectionInt = convertStringToInteger(lDirection);
        if (lDirection.isEmpty() && lDirectionInt == null) {
            mEditTextDirection.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextDirection.getId();
        } else {
            if (lDirectionInt < 0 || lDirectionInt > 360) {
                mEditTextDirection.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.please_insert_value_between_1_360));
                return mEditTextDirection.getId();
            }
        }
        return 0;
    }

    private Airplane assignValues() {
        Airplane lAirplane = new Airplane();
        PointDouble lPoint;

        float lCoordinateX;
        float lCoordinateY;
        Double lRadius;
        Double lTheta;
        float lDirection = parseFloat(mEditTextDirection.getText().toString());
        float lSpeed = parseFloat(mEditTextSpeed.getText().toString());


        if (mLayoutCartesian.getVisibility() == View.VISIBLE) {
            lCoordinateX = parseFloat(mEditTextCartesianX.getText().toString());
            lCoordinateY = parseFloat(mEditTextCartesianY.getText().toString());
            lPoint = convertCartesianToPolar(lCoordinateX, lCoordinateY);
            lRadius = lPoint.x;
            lTheta = lPoint.y;
        } else {
            lRadius = Double.valueOf(mEditTextRadius.getText().toString());
            lTheta = Double.valueOf(mEditTextTheta.getText().toString());
            lPoint = convertPolarToCartesian(lRadius, lTheta);
            lCoordinateX = lPoint.x.floatValue();
            lCoordinateY = lPoint.y.floatValue();
        }

        lAirplane.setCoordinateX(lCoordinateX);
        lAirplane.setCoordinateY(lCoordinateY);
        lAirplane.setTheta(lTheta.floatValue());
        lAirplane.setRadius(lRadius.floatValue());
        lAirplane.setDirection(lDirection);
        lAirplane.setSpeed(lSpeed);

        return lAirplane;
    }

    private void clearFields() {
        mSwitchTypeCoordinate.setText("");
        mEditTextCartesianX.setText("");
        mEditTextCartesianY.setText("");
        mEditTextRadius.setText("");
        mEditTextTheta.setText("");
        mEditTextSpeed.setText("");
        mEditTextDirection.setText("");
    }
}
