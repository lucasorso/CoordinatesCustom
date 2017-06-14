package com.orsomob.coordinates.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.AirplaneDetailsAdapter;
import com.orsomob.coordinates.data.module.AirplaneData;

import java.util.List;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class HistoryActivity extends AppCompatActivity implements AirplaneDetailsAdapter.RecyclerViewClickListener{

    private static final String TAG = "HISTORY_ACTIVITY";
    private AirplaneDetailsAdapter mAirplaneDetailsAdapter;
    private RecyclerView mRecyclerView;
    private List<AirplaneData> mAirplaneDataList;

    /**
     * Override methods
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAirplaneDetailsAdapter = new AirplaneDetailsAdapter(this, getSupportActionBar(), this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_hitory);

        LinearLayoutManager lLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(lLayoutManager);


        mRecyclerView.setAdapter(mAirplaneDetailsAdapter);
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
    public void recyclerViewListClicked(AirplaneData aAirplaneData, int aPostion) {
        Log.i(TAG, "Airplane Add: " + aAirplaneData.getName() + " Position: " + aPostion);
    }

    @Override
    public void recyclerViewListUnClicked(AirplaneData aAirplaneData, int aPostion) {
        Log.i(TAG, "Airplane Remove: " + aAirplaneData.getName() + " Position: " + aPostion);
    }

    /**
     * Non override methods
     */


}
