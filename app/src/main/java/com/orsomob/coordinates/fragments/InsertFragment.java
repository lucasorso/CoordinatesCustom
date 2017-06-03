package com.orsomob.coordinates.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.orsomob.coordinates.util.Function;

import static com.orsomob.coordinates.util.Function.convertPolarToCartesian;

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
    private TextInputEditText mEditTextPolarRadius;
    private TextInputEditText mEditTextPolarDegrees;
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
        mEditTextPolarRadius = (TextInputEditText) mView.findViewById(R.id.ed_radius);
        mEditTextPolarDegrees = (TextInputEditText) mView.findViewById(R.id.ed_degrees);
        mEditTextSpeed = (TextInputEditText) mView.findViewById(R.id.ed_speed);
        mEditTextDirection = (TextInputEditText) mView.findViewById(R.id.ed_direction);
    }

    private int validateValues() {

        String lCartesianX;
        String lCartesianY;
        String lRadius;
        String lDegrees;
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
                    showDialoAlert(getActivity().getResources().getString(R.string.max_value_10));
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
                    showDialoAlert(getActivity().getResources().getString(R.string.max_value_10));
                    return mEditTextCartesianX.getId();
                }
            }
        } else {
            /**
             * This means is a polar coordinates has inserted
             */
            lRadius = mEditTextPolarRadius.getText().toString();
            Integer lRadiusInt = convertStringToInteger(lRadius);
            if (lRadius.isEmpty() && lRadiusInt == null) {
                mEditTextPolarRadius.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextPolarRadius.getId();
            }
            lDegrees = mEditTextPolarDegrees.getText().toString();
            Integer lDegreesInt = convertStringToInteger(lDegrees);
            if (lDegrees.isEmpty() && lDegreesInt == null) {
                mEditTextPolarDegrees.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextPolarDegrees.getId();
            }
            /**
             * Need to be valid to proceed
             */
            Point lPoint = Function.convertPolarToCartesian(lRadiusInt.floatValue(), lDegreesInt.floatValue());
            if ((lPoint.x > 10 || lPoint.x < -10) &&
                    (lPoint.y > 10 || lPoint.y < -10)) {
                mEditTextPolarDegrees.setError(getActivity().getString(R.string.invalid_field));
                mEditTextPolarRadius.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(String.format(getActivity().getResources().getString(R.string.the_polar_coordinate_is_not_valid), lPoint.x, lPoint.y));
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
                showDialoAlert(getActivity().getResources().getString(R.string.please_insert_value_greater_than_0));
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
                showDialoAlert(getActivity().getResources().getString(R.string.please_insert_value_between_1_360));
                return mEditTextDirection.getId();
            }
        }
        return 0;
    }

    private Integer convertStringToInteger(String aStrinNumber) {
        Integer lInteger = null;
        try {
            lInteger = Integer.parseInt(aStrinNumber);
        } catch (NumberFormatException aE) {
            Log.e(TAG, "convertStringToInteger: " + aE.getMessage());
            aE.printStackTrace();
        }
        return lInteger;
    }

    private void showDialoAlert(String aMessage) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(getActivity());
        lBuilder.setTitle(R.string.alert).setMessage(aMessage);
        AlertDialog lAlertDialog = lBuilder.create();
        lAlertDialog.show();
    }

    private Airplane assignValues() {
        Airplane lAirplane = new Airplane();
        Point lPoint;

        float lCoordinateX;
        float lCoordinateY;
        float lRadius;
        float lDegrees;
        float lDirection = Float.parseFloat(mEditTextDirection.getText().toString());
        float lSpeed = Float.parseFloat(mEditTextSpeed.getText().toString());


        if (mLayoutCartesian.getVisibility() == View.VISIBLE) {
            lCoordinateX = Float.parseFloat(mEditTextCartesianX.getText().toString());
            lCoordinateY = Float.parseFloat(mEditTextCartesianY.getText().toString());
            lPoint = Function.convertCartesianToPolar(lCoordinateX, lCoordinateY);
            lDegrees = lPoint.x;
            lRadius = lPoint.y;
        } else {
            lDegrees = Float.parseFloat(mEditTextPolarDegrees.getText().toString());
            lRadius = Float.parseFloat(mEditTextPolarRadius.getText().toString());
            lPoint = convertPolarToCartesian(lRadius, lDegrees);
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

    private void clearFields() {
        mSwitchTypeCoordinate.setText("");
        mEditTextCartesianX.setText("");
        mEditTextCartesianY.setText("");
        mEditTextPolarRadius.setText("");
        mEditTextPolarDegrees.setText("");
        mEditTextSpeed.setText("");
        mEditTextDirection.setText("");
    }
}
