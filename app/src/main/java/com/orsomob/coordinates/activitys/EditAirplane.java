package com.orsomob.coordinates.activitys;

import android.content.Intent;
import android.graphics.Point;
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
import com.orsomob.coordinates.util.Function;

import static com.orsomob.coordinates.util.Function.convertStringToInteger;
import static com.orsomob.coordinates.util.Function.showDialoAlert;


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
    private Button mRemoveButton;
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
            case R.id.btn_edit: {
                if (validateValues() == 0) {
                    assignValues();
                    setResult(RESULT_OK);
                    Intent lIntent = new Intent();
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable("airplane", mAirplane);
                    lBundle.putBoolean("edit", true);
                    lIntent.putExtras(lBundle);
                    setResult(RESULT_OK, lIntent);
                    finish();
                } else {
                    Log.e(TAG, "Edit plane field validate !");
                }
            }
            break;
            case R.id.btn_remove: {
                Intent lIntent = new Intent();
                Bundle lBundle = new Bundle();
                lBundle.putSerializable("airplane", mAirplane);
                lBundle.putBoolean("remove", true);
                lIntent.putExtras(lBundle);
                setResult(RESULT_OK, lIntent);
                finish();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
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
        mRemoveButton = (Button) findViewById(R.id.btn_remove);
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
        mRemoveButton.setOnClickListener(this);
    }

    private void enableEdit() {
        mEditTextName.setEnabled(true);
        mEditTextCartesianX.setEnabled(true);
        mEditTextCartesianY.setEnabled(true);
//        mEditTextPolarRadius.setEnabled(true);
//        mEditTextPolarDegrees.setEnabled(true);
        mEditTextSpeed.setEnabled(true);
        mEditTextDirection.setEnabled(true);
        mEditButton.setEnabled(true);
        mEditButton.setVisibility(View.VISIBLE);
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
        mEditButton.setVisibility(View.INVISIBLE);
    }

    private int validateValues() {

        String lCartesianX;
        String lCartesianY;
        String lSpeed;
        String lDirection;

        lCartesianX = mEditTextCartesianX.getText().toString();
        Integer lCartesianXint = convertStringToInteger(lCartesianX);
        if (lCartesianX.isEmpty() && lCartesianXint == null) {
            mEditTextCartesianX.setError(EditAirplane.this.getResources().getString(R.string.invalid_field));
            return mEditTextCartesianX.getId();
        } else {
            if (lCartesianXint > 10 || lCartesianXint < -10) {
                mEditTextCartesianX.setError(this.getString(R.string.invalid_field));
                showDialoAlert(EditAirplane.this, EditAirplane.this.getResources().getString(R.string.max_value_10));
                return mEditTextCartesianX.getId();
            }
        }
        lCartesianY = mEditTextCartesianY.getText().toString();
        Integer lCartesianYint = convertStringToInteger(lCartesianY);
        if (lCartesianY.isEmpty() && lCartesianYint == null) {
            mEditTextCartesianY.setError(EditAirplane.this.getString(R.string.invalid_field));
            return mEditTextCartesianY.getId();
        } else {
            if (lCartesianYint > 10 || lCartesianYint < -10) {
                mEditTextCartesianY.setError(EditAirplane.this.getString(R.string.invalid_field));
                showDialoAlert(EditAirplane.this, EditAirplane.this.getResources().getString(R.string.max_value_10));
                return mEditTextCartesianX.getId();
            }
        }
        lSpeed = mEditTextSpeed.getText().toString();
        Integer lSpeedInt = convertStringToInteger(lSpeed);
        if (lSpeed.isEmpty() && lSpeedInt == null) {
            mEditTextSpeed.setError(EditAirplane.this.getString(R.string.invalid_field));
            return mEditTextSpeed.getId();
        } else {
            if (lSpeedInt <= 0) {
                mEditTextSpeed.setError(EditAirplane.this.getString(R.string.invalid_field));
                showDialoAlert(EditAirplane.this, EditAirplane.this.getResources().getString(R.string.please_insert_value_greater_than_0));
                return mEditTextSpeed.getId();
            }
        }
        lDirection = mEditTextDirection.getText().toString();
        Integer lDirectionInt = convertStringToInteger(lDirection);
        if (lDirection.isEmpty() && lDirectionInt == null) {
            mEditTextDirection.setError(EditAirplane.this.getString(R.string.invalid_field));
            return mEditTextDirection.getId();
        } else {
            if (lDirectionInt < 0 || lDirectionInt > 360) {
                mEditTextDirection.setError(EditAirplane.this.getString(R.string.invalid_field));
                showDialoAlert(EditAirplane.this, EditAirplane.this.getResources().getString(R.string.please_insert_value_between_1_360));
                return mEditTextDirection.getId();
            }
        }
        return 0;
    }

    private void assignValues() {
        String lName = mEditTextName.getText() != null &&
                !mEditTextName.getText().toString().isEmpty() ?
                mEditTextName.getText().toString() : mAirplane.getName();
        float lCoordinateX = Float.parseFloat(mEditTextCartesianX.getText().toString());
        float lCoordinateY = Float.parseFloat(mEditTextCartesianY.getText().toString());
        float lDirection = Float.parseFloat(mEditTextDirection.getText().toString());
        float lSpeed = Float.parseFloat(mEditTextSpeed.getText().toString());

        Point lPoint = Function.convertCartesianToPolar(lCoordinateX, lCoordinateY);

        mAirplane.setName(lName);
        mAirplane.setCoordinateX(lCoordinateX);
        mAirplane.setCoordinateY(lCoordinateY);
        mAirplane.setRadius((float) lPoint.x);
        mAirplane.setDegree((float) lPoint.y);
        mAirplane.setDirection(lDirection);
        mAirplane.setSpeed(lSpeed);
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
