package com.orsomob.coordinates.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.AirplaneDetailsAdapter;
import com.orsomob.coordinates.db.AirplaneDB;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.property.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class HistoryActivity extends AppCompatActivity implements AirplaneDetailsAdapter.RecyclerViewClickListener {

    private static final String TAG = "HISTORY_ACTIVITY";
    private List<AirplaneDB> mAirplaneDBList;

    /**
     * Override methods
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_traslation:
                break;
            case R.id.menu_rotation:
                break;
            case R.id.menu_remove:
                sendListToRemove();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendListToRemove() {
        Intent lIntent = new Intent();
        Bundle lBundle = new Bundle();
        List<Airplane> lAirplanes = Airplane.loadListFromDataBase(mAirplaneDBList);
        lBundle.putInt("option", Option.REMOVE_AIRPLANE);
        lBundle.putSerializable("list", (Serializable) lAirplanes);
        lIntent.putExtras(lBundle);
        setResult(RESULT_OK, lIntent);
        finish();
    }

    @Override
    public void recyclerViewListClicked(AirplaneDB aAirplaneDB, int aPostion) {
        Log.i(TAG, "Airplane Add: " + aAirplaneDB.getName() + " Position: " + aPostion);
        mAirplaneDBList.add(aAirplaneDB);
    }

    @Override
    public void recyclerViewListUnClicked(AirplaneDB aAirplaneDB, int aPostion) {
        Log.i(TAG, "Airplane Remove: " + aAirplaneDB.getName() + " Position: " + aPostion);
        mAirplaneDBList.remove(aAirplaneDB);
    }

    /**
     * Non override methods
     */

    private void init() {
        mAirplaneDBList = new ArrayList<>();
        AirplaneDetailsAdapter lAirplaneDetailsAdapter = new AirplaneDetailsAdapter(this, getSupportActionBar(), this);
        RecyclerView lRecyclerView = (RecyclerView) findViewById(R.id.rv_hitory);
        LinearLayoutManager lLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lRecyclerView.setLayoutManager(lLayoutManager);
        lRecyclerView.setAdapter(lAirplaneDetailsAdapter);
    }

}
