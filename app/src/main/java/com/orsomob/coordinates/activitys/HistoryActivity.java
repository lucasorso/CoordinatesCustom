package com.orsomob.coordinates.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.AirplaneDetailsAdapter;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class HistoryActivity extends AppCompatActivity {

    private AirplaneDetailsAdapter mAirplaneDetailsAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAirplaneDetailsAdapter = new AirplaneDetailsAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_hitory);

        LinearLayoutManager lLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(lLayoutManager);

        mRecyclerView.setAdapter(mAirplaneDetailsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
