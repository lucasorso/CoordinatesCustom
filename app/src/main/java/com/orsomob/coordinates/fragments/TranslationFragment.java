package com.orsomob.coordinates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

public class TranslationFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ROTATION_FRAGMENT";
    private View mView;
    private AirplaneTranslate mAirplaneTranslate;
    private List<Airplane> mAirplaneList;
    private AirplaneSpinnerAdapter mSpinnerAdapter;
    private Spinner mSpinner;

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
                Toast.makeText(getActivity(), "TESTE TRANSLATION", Toast.LENGTH_SHORT).show();
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

    public interface AirplaneTranslate {
        void onTranslateAirplane(Double aX, Double aY, Double aDregree, Airplane aAirplane);
    }

    private void init() {
        setHasOptionsMenu(true);
        getReferences();
        setEvents();

        mAirplaneList = new ArrayList<>();
        List<AirplaneData> lAirplaneDatas = SQLite.select().from(AirplaneData.class).queryList();
        mAirplaneList = Airplane.loadListFromDataBase(lAirplaneDatas);
        mSpinnerAdapter = new AirplaneSpinnerAdapter(getActivity(), mAirplaneList);
        mSpinner.setAdapter(mSpinnerAdapter);

    }

    private void setEvents() {
    }

    public void getReferences() {
        mSpinner = (Spinner) mView.findViewById(R.id.sp_rotation);
    }

}
