package com.orsomob.coordinates.fragments;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.Toast;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;

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
    private TextInputEditText mEditTextpolarAngle;
    private TextInputEditText mEditTextSpeed;
    private TextInputEditText mEditTextDirection;
    private PassAirplaneListener mAirplaneListener;
    private Airplane mAirplane;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAirplaneListener = (PassAirplaneListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PassAirplaneListener");
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
                Toast.makeText(getActivity(), "TESTE BUTTON FRAG", Toast.LENGTH_SHORT).show();
                sendAirplane(mAirplane);
                /*if (validateValues() == 0) {
                    assignValues();
                    sendAirplane(mAirplane);
                }*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface PassAirplaneListener {
        void getAirplane(Airplane aAirplane);
    }

    public void sendAirplane(Airplane aAirplane) {
        if (mAirplaneListener != null) {
            mAirplaneListener.getAirplane(aAirplane);
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
        mEditTextpolarAngle = (TextInputEditText) mView.findViewById(R.id.ed_angle);
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
            String lAngle = mEditTextpolarAngle.getText().toString();
            if (lAngle.isEmpty() && TextUtils.isDigitsOnly(lAngle)) {
                mEditTextpolarAngle.setError(getActivity().getString(R.string.invalid_field));
                return mEditTextpolarAngle.getId();
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

    private void assignValues() {
        mAirplane = new Airplane();
        mAirplane.setCoordinateX(4L);
        mAirplane.setCoordinateY(-4L);
    }
}
