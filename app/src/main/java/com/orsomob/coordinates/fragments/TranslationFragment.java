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

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.activitys.MainActivity;
import com.orsomob.coordinates.helper.AirplaneDetailHelper;
import com.orsomob.coordinates.interfaces.AirplaneTouch;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;

import static com.orsomob.coordinates.util.Function.showDialoAlert;
import static com.orsomob.coordinates.util.Function.translatePoint;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class TranslationFragment extends Fragment implements AirplaneTouch {

    public static final String TAG = "ROTATION_FRAGMENT";

    private View mView;
    private AirplaneTranslate mAirplaneTranslate;
    private TextInputEditText mEditTextPertcent;
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

                if (mAirplaneToSend != null) {
                    PointDouble lPointDouble = updateCoordinates(mEditTextPertcent.getText().toString());
                    if (lPointDouble != null) {
                        mAirplaneToSend.setCoordinateX(lPointDouble.getX().floatValue());
                        mAirplaneToSend.setCoordinateY(lPointDouble.getY().floatValue());
                        mAirplaneTranslate.onTranslateAirplane(mAirplaneToSend);
                    }
                } else {
                    showDialoAlert(getActivity(), getActivity().getResources().getString(R.string.select_airplane_to_continue));
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

    private PointDouble updateCoordinates(String aValue) {
        // DONE: 6/14/17 Realizar tratamento para não sai da tela.
        PointDouble lPointDouble = translatePoint(mAirplaneToSend, aValue);
        if (lPointDouble != null) {
            if (lPointDouble.getX() > 10 || lPointDouble.getX() < -10 ||
                    lPointDouble.getY() > 10 || lPointDouble.getY() < -10) {
                showDialoAlert(getActivity(), String.format(getActivity().getResources().getString(R.string.the_percent_is_not_valid), lPointDouble.x.intValue(), lPointDouble.y.intValue()));
                return null;
            }
        }
        return lPointDouble;
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
        mAirplaneDetailHelper.getLayout().setVisibility(View.INVISIBLE);
    }

    public void getReferences() {
        mEditTextPertcent = (TextInputEditText) mView.findViewById(R.id.ed_percent);
        mAirplaneDetailHelper = new AirplaneDetailHelper(mView);
    }

}
