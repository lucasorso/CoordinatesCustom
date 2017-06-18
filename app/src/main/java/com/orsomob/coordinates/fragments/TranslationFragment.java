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
import android.widget.Toast;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.activitys.MainActivity;
import com.orsomob.coordinates.interfaces.AirplaneTouch;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.util.Function;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class TranslationFragment extends Fragment implements AirplaneTouch {

    public static final String TAG = "ROTATION_FRAGMENT";

    private View mView;
    private AirplaneTranslate mAirplaneTranslate;
    private TextInputEditText mEditTextPertcent;
    private Airplane mAirplaneToSend;

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

    @Override
    public void onAirplaneTouch(Airplane aAirplane) {
        Toast.makeText(getActivity(), "Translation Fragment", Toast.LENGTH_SHORT).show();
        mAirplaneToSend = aAirplane;
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

    }

    private void setEvents() {
        mEditTextPertcent.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditTextPertcent.setText("0");
    }

    public void getReferences() {
        mEditTextPertcent = (TextInputEditText) mView.findViewById(R.id.ed_percent);
    }

}
