package com.orsomob.coordinates.fragments;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.AirplaneSpinnerAdapter;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.module.Airplane;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class RotationFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ROTATION_FRAGMENT";
    private View mView;
    private AirplaneRotate mAirplaneRotate;
    private List<Airplane> mAirplaneList;
    private AirplaneSpinnerAdapter mSpinnerAdapter;
    private Spinner mSpinner;
    private TextInputEditText mEditTextCartesianX;
    private TextInputEditText mEditTextCartesianY;
    private TextInputEditText mEditTextPolarDegrees;
    private Button mButtonRotate;
    private GraphView mGraphView;

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
                Toast.makeText(getActivity(), "TESTE ROTATION", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rotate:

//                mAirplaneRotate.onRotateAirplane();
                break;
        }
    }

    public interface AirplaneRotate {
        void onRotateAirplane(Double aX, Double aY, Double aDregree, Airplane aAirplane);
    }

    private void init() {
        setHasOptionsMenu(true);
        mGraphView = (GraphView) getArguments().get("graph_view");
        getReferences();
        setEvents();
        configureAdapter();

    }

    private void configureAdapter() {
        List<AirplaneData> lAirplaneDatas = SQLite.select().from(AirplaneData.class).queryList();
        mAirplaneList = new ArrayList<>();
        mAirplaneList = Airplane.loadListFromDataBase(lAirplaneDatas);
        mSpinnerAdapter = new AirplaneSpinnerAdapter(getActivity(), mAirplaneList);
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    private void setEvents() {
        mButtonRotate.setOnClickListener(this);
    }

    public void getReferences() {
        mEditTextCartesianX = (TextInputEditText) mView.findViewById(R.id.ed_x);
        mEditTextCartesianY = (TextInputEditText) mView.findViewById(R.id.ed_y);
        mEditTextPolarDegrees = (TextInputEditText) mView.findViewById(R.id.ed_degrees);
        mButtonRotate = (Button) mView.findViewById(R.id.btn_rotate);
        mSpinner = (Spinner) mView.findViewById(R.id.sp_rotation);
    }

    private Matrix getRotationMatrix(Point aPoint, float aAngle) {
        Matrix matrix = new Matrix();
//        imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate(aAngle, aPoint.x, aPoint.y);
//        imageView.setImageMatrix(matrix);
        return matrix;
    }

}
