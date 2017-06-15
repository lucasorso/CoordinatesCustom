package com.orsomob.coordinates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.AirplaneSpinnerAdapter;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.util.Function;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class TranslationFragment extends Fragment {

    public static final String TAG = "ROTATION_FRAGMENT";

    private View mView;
    private AirplaneTranslate mAirplaneTranslate;
    private List<Airplane> mAirplaneList;
    private AirplaneSpinnerAdapter mSpinnerAdapter;
    private Spinner mSpinner;
    private TextInputEditText mEditTextPertcent;
    private Airplane mAirplaneToSend;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAirplaneTranslate = (AirplaneTranslate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AirplaneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_translation, container, false);
        init();
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.translation_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_translation:
                Airplane lAirplane = updateCoordinates(mEditTextPertcent.getText().toString());
                mAirplaneTranslate.onTranslateAirplane(lAirplane);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Airplane updateCoordinates(String aValue) {
        // TODO: 6/14/17 Realizar tratamento para não sai da tela.
        Float lfloatValue = Function.convertStringToFloat(aValue);
        if (lfloatValue != null) {
            Float lPercent = lfloatValue / 100;

            float lCoordX = mAirplaneToSend.getCoordinateX() + (mAirplaneToSend.getCoordinateX() * lPercent);
            float lCoordY = mAirplaneToSend.getCoordinateX() + (mAirplaneToSend.getCoordinateY() * lPercent);

            mAirplaneToSend.setCoordinateX(lCoordX);
            mAirplaneToSend.setCoordinateY(lCoordY);

        }
        return mAirplaneToSend;
    }


    public interface AirplaneTranslate {
        void onTranslateAirplane(Airplane aAirplane);
    }

    private void init() {
        setHasOptionsMenu(true);
        getReferences();
        setEvents();
        configureSpinner();

    }

    private void configureSpinner() {
        mAirplaneList = new ArrayList<>();
        List<AirplaneData> lAirplaneDatas = SQLite.select().from(AirplaneData.class).queryList();
        mAirplaneList = Airplane.loadListFromDataBase(lAirplaneDatas);
        mSpinnerAdapter = new AirplaneSpinnerAdapter(getActivity(), mAirplaneList);
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    private void setEvents() {
        mEditTextPertcent.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditTextPertcent.setText("0");

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAirplaneToSend = mSpinnerAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAirplaneToSend = mSpinnerAdapter.getItem(0);
            }
        });
    }

    public void getReferences() {
        mSpinner = (Spinner) mView.findViewById(R.id.sp_rotation);
        mEditTextPertcent = (TextInputEditText) mView.findViewById(R.id.ed_percent);
    }

}
