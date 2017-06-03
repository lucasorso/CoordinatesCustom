package com.orsomob.coordinates.activitys;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;

/**
 * Created by LucasOrso on 6/3/17.
 */

public class EditAirplane extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EDIT_AIRPLANE";

    private Switch mSwitchEdit;
    private TextInputEditText mEditTextName;
    private TextInputEditText mEditTextCartesianX;
    private TextInputEditText mEditTextCartesianY;
    private TextInputEditText mEditTextPolarDegrees;
    private TextInputEditText mEditTextPolarRadius;
    private TextInputEditText mEditTextSpeed;
    private TextInputEditText mEditTextDirection;
    private ImageView mImageViewAirplane;
    private Button mEditButton;
    private Airplane mAirplane;

    /**
     * Override methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_airplane);
        init();
    }

    @Override
    protected void onResume() {
        ScrollView lScrollView = (ScrollView) findViewById(R.id.scrollView_edit);
        lScrollView.requestFocus();
        lScrollView.clearFocus();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                Airplane lAirplane = null;
                // TODO: 6/3/17 Fazer
                setResult(RESULT_OK);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * Non Override methods
     */

    private void init() {
        setTitle("Edit Airplane");
        Bundle lBundle = getIntent().getExtras();
        if (lBundle != null && lBundle.containsKey("airplane")) {
            mAirplane = (Airplane) lBundle.get("airplane");
        } else {
            Log.e(TAG, "init: Airplane not found in bundle !");
            finish();
        }
        getReferences();
        setEvents();
        setValues();
    }

    private void getReferences() {
        mSwitchEdit = (Switch) findViewById(R.id.sw_edit);
        mImageViewAirplane = (ImageView) findViewById(R.id.iv_airplane);
        mEditTextName = (TextInputEditText) findViewById(R.id.ed_name);
        mEditTextCartesianX = (TextInputEditText) findViewById(R.id.ed_x);
        mEditTextCartesianY = (TextInputEditText) findViewById(R.id.ed_y);
        mEditTextPolarRadius = (TextInputEditText) findViewById(R.id.ed_radius);
        mEditTextPolarDegrees = (TextInputEditText) findViewById(R.id.ed_degrees);
        mEditTextSpeed = (TextInputEditText) findViewById(R.id.ed_speed);
        mEditTextDirection = (TextInputEditText) findViewById(R.id.ed_direction);
        mEditButton = (Button) findViewById(R.id.btn_edit);
    }

    private void setEvents() {
        mSwitchEdit.setChecked(false);
        disableEdit();
        mSwitchEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /*Edit*/
                    enableEdit();
                } else {
                    /*Not Edit*/
                    disableEdit();
                }
            }
        });
        mEditButton.setOnClickListener(this);
    }

    private void enableEdit() {
        mEditTextName.setEnabled(true);
        mEditTextCartesianX.setEnabled(true);
        mEditTextCartesianY.setEnabled(true);
        mEditTextPolarRadius.setEnabled(true);
        mEditTextPolarDegrees.setEnabled(true);
        mEditTextSpeed.setEnabled(true);
        mEditTextDirection.setEnabled(true);
        mEditButton.setEnabled(true);
    }

    private void disableEdit() {
        mEditTextName.setEnabled(false);
        mEditTextCartesianX.setEnabled(false);
        mEditTextCartesianY.setEnabled(false);
        mEditTextPolarRadius.setEnabled(false);
        mEditTextPolarDegrees.setEnabled(false);
        mEditTextSpeed.setEnabled(false);
        mEditTextDirection.setEnabled(false);
        mEditButton.setEnabled(false);
    }

    private void setValues() {
        mEditTextName.setText(mAirplane.getName() != null ? mAirplane.getName() : "");
        mImageViewAirplane.setRotation(mAirplane.getDirection() != null ? mAirplane.getDirection() : 0.0f);
        mEditTextCartesianX.setText(mAirplane.getCoordinateX() != null ? String.valueOf(mAirplane.getCoordinateX()) : "");
        mEditTextCartesianY.setText(mAirplane.getCoordinateY() != null ? String.valueOf(mAirplane.getCoordinateY()) : "");
        mEditTextPolarRadius.setText(mAirplane.getRadius() != null ? String.valueOf(mAirplane.getRadius()) : "");
        mEditTextPolarDegrees.setText(mAirplane.getDegree() != null ? String.valueOf(mAirplane.getDegree()) : "");
        mEditTextSpeed.setText(mAirplane.getSpeed() != null ? String.valueOf(mAirplane.getSpeed()) : "");
        mEditTextDirection.setText(mAirplane.getDirection() != null ? String.valueOf(mAirplane.getDirection()) : "");
    }
}
