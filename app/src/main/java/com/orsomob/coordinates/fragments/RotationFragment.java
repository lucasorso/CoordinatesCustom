package com.orsomob.coordinates.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private TextInputEditText mEditTextPolarDegrees;
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
                    Double lCoordinateX;
                    Double lCoordinateY;
                    Double lDegrees;

                    lCoordinateX = Double.valueOf(mEditTextCartesianX.getText().toString());
                    lCoordinateY = Double.valueOf(mEditTextCartesianY.getText().toString());
                    lDegrees = Double.valueOf(mEditTextPolarDegrees.getText().toString());

                    /*Point lPointToRate = new Point();
                    lPointToRate.set(lCoordinateX.intValue(), lCoordinateY.intValue());

                    Point lAtualPoint = new Point();
                    lAtualPoint.set(mAirplaneToSend.getCoordinateX().intValue(), mAirplaneToSend.getCoordinateY().intValue());

                    rotateAround(lPointToRate, lAtualPoint, lDegrees);

                    mAirplaneRotate.onRotateAirplane(lCoordinateX.floatValue(), lCoordinateY.floatValue(), lDegrees.floatValue(), mAirplaneToSend);*/


                    PointDouble lPointToRotate = new PointDouble();
                    lPointToRotate.set(lCoordinateX, lCoordinateY);

                    PointDouble lAtualPoint = new PointDouble();
                    lAtualPoint.set(mAirplaneToSend.getCoordinateX().doubleValue(), mAirplaneToSend.getCoordinateY().doubleValue());

                    PointDouble lNewPoint = rotationPoint(lPointToRotate, lAtualPoint, lDegrees);

                    if (lNewPoint != null) {
                        mAirplaneRotate.onRotateAirplane(lCoordinateX.floatValue(), lCoordinateY.floatValue(), lDegrees.floatValue(), mAirplaneToSend, lNewPoint);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: 6/17/17 Remover feito para testes
    private void rotateAround(Point center, Point point, double angle) {

        /*double x1 = point.x - center.x;
        double y1 = point.y - center.y;

        double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle));
        double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle));

        point.x = x2 + center.x;
        point.y = y2 + center.y;*/

        double x1 = point.x - center.x;
        double y1 = point.y - center.y;

        double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
        double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

        point.x = (int) (x2 + center.x);
        point.y = (int) (y2 + center.y);

//        Double lNewX = (center.x + (Math.cos(Math.toRadians(angle)) * (x - center.x) - Math.sin(Math.toRadians(angle)) * (y - center.y)));
        Log.i(TAG, "new X " + point.x);
//        Double lNewY = (center.y + (Math.sin(Math.toRadians(angle)) * (x - center.x) + Math.cos(Math.toRadians(angle)) * (y - center.y)));
        Log.i(TAG, "new Y " + point.y);
    }

    @Override
    public void onAirplaneTouch(Airplane aAirplane) {
        mAirplaneToSend = aAirplane;

        mAirplaneDetailHelper.setAiplane(aAirplane);

        if (mAirplaneDetailHelper.getLayout().getVisibility() == View.INVISIBLE){
            mAirplaneDetailHelper.getLayout().setVisibility(View.VISIBLE);
        }

        //up
        //mAirplaneDetailHelper.getLayout().animate().translationY(0).setDuration(5000);
        //down
        //mAirplaneDetailHelper.getLayout().animate().translationY(mAirplaneDetailHelper.getLayout().getHeight()).alpha(1.0f).setDuration(3000);

        /**
         * Animation working
         */
        if (mAirplaneDetailHelper.getLayout().getVisibility() == View.INVISIBLE) {
            mAirplaneDetailHelper.getLayout().setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAirplaneDetailHelper.getLayout().animate().alpha(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAirplaneDetailHelper.getLayout().setVisibility(View.INVISIBLE);
                            mAirplaneDetailHelper.getLayout().setAlpha(1.0f);
                        }
                    });

                }
            }, 3000);
        }
    }

    public interface AirplaneRotate {
        void onRotateAirplane(Float aX, Float aY, Float aDregree, Airplane aAirplane, PointDouble lNewPoint);
    }

    private void init() {
        setHasOptionsMenu(true);
        getReferences();
    }

    public void getReferences() {
        mEditTextCartesianX = (TextInputEditText) mView.findViewById(R.id.ed_x);
        mEditTextCartesianY = (TextInputEditText) mView.findViewById(R.id.ed_y);
        mEditTextPolarDegrees = (TextInputEditText) mView.findViewById(R.id.ed_degrees);
        mAirplaneDetailHelper = new AirplaneDetailHelper(mView);
        mAirplaneDetailHelper.getLayout().setVisibility(View.INVISIBLE);
    }

    private int validateValues() {

        String lX;
        String lY;
        String lDegrees;

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
        lDegrees = mEditTextPolarDegrees.getText().toString();
        Integer lDegreesInt = convertStringToInteger(lDegrees);
        if (lDegrees.isEmpty() && lDegreesInt == null) {
            mEditTextPolarDegrees.setError(getActivity().getString(R.string.invalid_field));
            return mEditTextPolarDegrees.getId();
        }
        return 0;
    }

}
