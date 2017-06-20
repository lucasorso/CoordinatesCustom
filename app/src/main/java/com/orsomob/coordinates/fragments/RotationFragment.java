package com.orsomob.coordinates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.activitys.MainActivity;
import com.orsomob.coordinates.helper.AirplaneDetailHelper;
import com.orsomob.coordinates.interfaces.AirplaneTouch;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;

import static com.orsomob.coordinates.util.Function.convertStringToInteger;
import static com.orsomob.coordinates.util.Function.rotationPoint;
import static com.orsomob.coordinates.util.Function.showDialoAlert;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class RotationFragment extends Fragment implements AirplaneTouch {

    public static final String TAG = "ROTATION_FRAGMENT";

    private View mView;
    private AirplaneRotate mAirplaneRotate;
    private TextInputEditText mEditTextCartesianX;
    private TextInputEditText mEditTextCartesianY;
    private TextInputEditText mEditTextPolarthetas;
    private Airplane mAirplaneToSend;
    private AirplaneDetailHelper mAirplaneDetailHelper;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded()) {
            ((MainActivity) getActivity()).setListenerFragment(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAirplaneRotate = (AirplaneRotate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AirplaneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rotation, container, false);
        init();
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.rotate_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rotate:
                if (validateValues() == 0) {

                    if (mAirplaneToSend != null) {
                        Double lCoordinateX;
                        Double lCoordinateY;
                        Double lTheta;

                        lCoordinateX = Double.valueOf(mEditTextCartesianX.getText().toString());
                        lCoordinateY = Double.valueOf(mEditTextCartesianY.getText().toString());
                        lTheta = Double.valueOf(mEditTextPolarthetas.getText().toString());

                        PointDouble lPointToRotate = new PointDouble();
                        lPointToRotate.set(lCoordinateX, lCoordinateY);

                        PointDouble lAtualPoint = new PointDouble();
                        lAtualPoint.set(mAirplaneToSend.getCoordinateX().doubleValue(), mAirplaneToSend.getCoordinateY().doubleValue());

                        PointDouble lNewPoint = rotationPoint(lPointToRotate, lAtualPoint, lTheta);

                        if (lNewPoint != null) {
                            mAirplaneRotate.onRotateAirplane(lCoordinateX.floatValue(), lCoordinateY.floatValue(), lTheta.floatValue(), mAirplaneToSend, lNewPoint);
                        }
                    } else {
                        showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.select_airplane_to_continue));
                    }

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAirplaneTouch(Airplane aAirplane) {
        mAirplaneToSend = aAirplane;

        mAirplaneDetailHelper.setAiplane(aAirplane);

        mAirplaneDetailHelper.animate();

    }

    public interface AirplaneRotate {
        void onRotateAirplane(Float aX, Float aY, Float aDregree, Airplane aAirplane, PointDouble lNewPoint);
    }

    private void init() {
        setHasOptionsMenu(true);
        getReferences();
        setEvents();
    }

    public void getReferences() {
        mEditTextCartesianX = (TextInputEditText) mView.findViewById(R.id.ed_x);
        mEditTextCartesianY = (TextInputEditText) mView.findViewById(R.id.ed_y);
        mEditTextPolarthetas = (TextInputEditText) mView.findViewById(R.id.ed_theta);
        mAirplaneDetailHelper = new AirplaneDetailHelper(mView);
    }

    private void setEvents() {
        mAirplaneDetailHelper.getLayout().setVisibility(View.INVISIBLE);
    }

    private int validateValues() {

        String lX;
        String lY;
        String lTheta;

        lX = mEditTextCartesianX.getText().toString();
        Integer lXint = convertStringToInteger(lX);
        if (lX.isEmpty() && lXint == null) {
            mEditTextCartesianX.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextCartesianX.getId();
        } else {
            if (lXint > 10 || lXint < -10) {
                mEditTextCartesianX.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.max_value_10));
                return mEditTextCartesianX.getId();
            }
        }
        lY = mEditTextCartesianY.getText().toString();
        Integer lYint = convertStringToInteger(lY);
        if (lY.isEmpty() && lYint == null) {
            mEditTextCartesianY.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextCartesianY.getId();
        } else {
            if (lYint > 10 || lYint < -10) {
                mEditTextCartesianY.setError(getActivity().getString(R.string.invalid_field));
                showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.max_value_10));
                return mEditTextCartesianX.getId();
            }
        }
        lTheta = mEditTextPolarthetas.getText().toString();
        Integer lThetaInt = convertStringToInteger(lTheta);
        if (lTheta.isEmpty() && lThetaInt == null) {
            mEditTextPolarthetas.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextPolarthetas.getId();
        }
        return 0;
    }

}
