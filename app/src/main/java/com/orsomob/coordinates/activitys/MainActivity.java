package com.orsomob.coordinates.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.ViewPagerAdapter;
import com.orsomob.coordinates.animations.ZoomOutPageTransformer;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.data.module.AirplaneData_Table;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.fragments.RotationFragment;
import com.orsomob.coordinates.fragments.TranslationFragment;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.util.AirplaneView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements InsertFragment.AirplaneListener, RotationFragment.AirplaneRotate, TranslationFragment.AirplaneTranslate, View.OnTouchListener {

    private static final int EDIT = 10;
    private static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private List<Airplane> mAirplaneList;
    private ViewPager mViewPager;
    private AirplaneView mAirplaneView;

    /**
     * Override methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        init();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_historico:
                startActivityForResult(new Intent(this, HistoryActivity.class), HISTORY);
                break;
            case R.id.menu_rotation:
                startActivity(new Intent(this, RotationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT: {
                if (RESULT_OK == resultCode) {
                    Bundle lBundle = data.getExtras();
                    if (lBundle != null) {
                        boolean lEdit = lBundle.containsKey("edit") && lBundle.getBoolean("edit");
                        boolean lRemove = lBundle.containsKey("remove") && lBundle.getBoolean("remove");
                        Airplane lAirplane = lBundle.containsKey("airplane") ? (Airplane) lBundle.get("airplane") : null;
                        if (lEdit) {
                            editAirplane(lAirplane);
                        }
                        if (lRemove) {
                            removeAirplane(lAirplane);
                        }
                    }
                }
            }
            break;
            case HISTORY:
                // TODO: 6/7/17 FAZER
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onReceiveAirplane(final Airplane aAirplane) {

        aAirplane.setName(aAirplane.getName() + mAirplaneList.size());
        int id = Airplane.saveToDatabase(aAirplane).getId();
        aAirplane.setId(id);

        ImageView lImageView = new ImageView(this);

        lImageView = addAirplaneToView(lImageView, aAirplane);

//        lImageView.setImageBitmap(mAirplaneView.getBitmap());
//        lImageView.setX(mGraphView.interpX(aAirplane.getCoordinateX()));
//        lImageView.setY(mGraphView.interpY(aAirplane.getCoordinateY()));
//        lImageView.setRotation(aAirplane.getDirection());
//        lImageView.setTag(aAirplane);
//        lImageView.setOnTouchListener(this);

        final ImageView finalLImageView = lImageView;

        mGraphView.post(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.addView(finalLImageView);
            }
        });

        mAirplaneList.add(aAirplane);

        showMessaUndo(aAirplane);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            Airplane lAirplane = (Airplane) v.getTag();
            openEdit(lAirplane);
        } catch (ClassCastException aE) {
            Log.e(TAG, "onTouch: Couldn`t catch aiplane");
            aE.printStackTrace();
        }
        return false;
    }

    @Override
    public void onRotateAirplane(Double aX, Double aY, Double aDregree, Airplane aAirplane) {

    }

    @Override
    public void onTranslateAirplane(Airplane aAirplane) {
        editAirplane(aAirplane);
    }

    /**
     * Non override methods
     */

    private void init() {
        getReferences();
        setEvents();
        getFromDataBase();
//        funTest(); // TODO: 6/3/17 REMOVER
    }

    private void getFromDataBase() {

        final List<AirplaneData> lAirplaneDatas = SQLite.select().from(AirplaneData.class).queryList();

        Handler lHandler = new Handler();

        lHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!lAirplaneDatas.isEmpty()) {

                    for (AirplaneData aAirplaneData : lAirplaneDatas) {
                        ImageView lImageView = new ImageView(MainActivity.this);
                        Airplane lAirplane = Airplane.loadFromDatabase(aAirplaneData);

                        lImageView = addAirplaneToView(lImageView, aAirplaneData);

//                        lImageView.setImageBitmap(mAirplaneView.getBitmap());
//                        lImageView.setX(mGraphView.interpX(lAirplane.getCoordinateX()));
//                        lImageView.setY(mGraphView.interpY(lAirplane.getCoordinateY()));
//                        lImageView.setRotation(lAirplane.getDirection());
//                        lImageView.setTag(lAirplane);
//                        lImageView.setOnTouchListener(MainActivity.this);

                        final ImageView finalLImageView = lImageView;
                        mGraphView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRelativeLayout.addView(finalLImageView);
                            }
                        });
                    }
                }
            }
        }, 1000);
    }

    private void funTest() {
        ImageView lImageView = new ImageView(this);
        Airplane lAirplane = new Airplane();

        lAirplane.setCoordinateX(5.0f);
        lAirplane.setCoordinateY(-5.0f);
        lAirplane.setDegree(7.0f);
        lAirplane.setDirection(45.0f);
        lAirplane.setRadius(0.0f);
        lAirplane.setSpeed(1000.0f);
        lAirplane.setName("Airplane Teste");

        lImageView = addAirplaneToView(lImageView, lAirplane);

//        lImageView.setImageBitmap(mAirplaneView.getBitmap());
//        lImageView.setX(mGraphView.interpX(lAirplane.getCoordinateX()));
//        lImageView.setY(mGraphView.interpY(lAirplane.getCoordinateY()));
//        lImageView.setRotation(lAirplane.getDirection());
//        lImageView.setTag(lAirplane);
//        lImageView.setOnTouchListener(this);

        Handler mHandler = new Handler();

        final ImageView finalLImageView = lImageView;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mGraphView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRelativeLayout.addView(finalLImageView);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 30000);
    }

    private void getReferences() {
        mAirplaneList = new ArrayList<>();
        mAirplaneView = new AirplaneView(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
    }

    private void setEvents() {
        configureViewPager();
    }

    private void configureViewPager() {

        Bundle lBundle = new Bundle();
        lBundle.putSerializable("graph_view",  mGraphView);

        RotationFragment lRotationFragment = new RotationFragment();
        lRotationFragment.setArguments(lBundle);

        ViewPagerAdapter lViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        lViewPagerAdapter.addFrag(new InsertFragment());
        lViewPagerAdapter.addFrag(lRotationFragment);
        lViewPagerAdapter.addFrag(new TranslationFragment());

        mViewPager.setAdapter(lViewPagerAdapter);
        mViewPager.offsetLeftAndRight(0);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setFitsSystemWindows(true);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    public RelativeLayout getRootLayout() {
        return mRelativeLayout;
    }

    private void openEdit(Airplane aAirplane) {
        Intent lIntent = new Intent(this, EditAirplane.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable("airplane", aAirplane);
        lIntent.putExtras(lBundle);
        startActivityForResult(lIntent, EDIT);
    }

    private void showMessaUndo(final Airplane aAirplane) {
        Snackbar lSnackbar = Snackbar
                .make(this.getRootLayout().findFocus(), "Airplane added !", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAirplane(aAirplane);
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        lSnackbar.show();
    }

    private ImageView getAirplaneFromView(Airplane aAirplane) {
        ImageView lImageView = null;
        Airplane lAirplane;
        int childCount = mRelativeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View lChildAt = mRelativeLayout.getChildAt(i);
            if (lChildAt instanceof ImageView) {
                lAirplane = (Airplane) lChildAt.getTag();
                if (Objects.equals(lAirplane.getId(), aAirplane.getId())) {
                    lImageView = (ImageView) lChildAt;
                }
            }
        }
        return lImageView;
    }

    private void editAirplane(final Airplane aAirplane) {
        final View lView = getAirplaneFromView(aAirplane);
        if (lView != null) {

            lView.setX(mGraphView.interpX(aAirplane.getCoordinateX()));
            lView.setY(mGraphView.interpY(aAirplane.getCoordinateY()));
            lView.setTag(aAirplane);
            lView.setOnTouchListener(MainActivity.this);
            lView.setRotation(aAirplane.getDirection());

            mRelativeLayout.invalidate(); // Update RootView

            AirplaneData lAirplaneData = Airplane.loadFromDataBase(aAirplane);
            lAirplaneData.setName(aAirplane.getName());
            lAirplaneData.setCoordinateX(Double.valueOf(aAirplane.getCoordinateX()));
            lAirplaneData.setCoordinateY(Double.valueOf(aAirplane.getCoordinateY()));
            lAirplaneData.setDegree(Double.valueOf(aAirplane.getDegree()));
            lAirplaneData.setDirection(Double.valueOf(aAirplane.getDirection()));
            lAirplaneData.setRadius(Double.valueOf(aAirplane.getRadius()));
            lAirplaneData.setSpeed(Double.valueOf(aAirplane.getSpeed()));
            lAirplaneData.update(); // UpdateDabase

        } else {
            Log.e(TAG, "View not found to edit !");
        }
    }

    private void removeAirplane(Airplane aAirplane) {
        View lView = getAirplaneFromView(aAirplane);
        if (lView != null) {
            mRelativeLayout.removeView(lView); // Remove from screen
            SQLite.delete().from(AirplaneData.class).where(AirplaneData_Table.id.is(aAirplane.getId())).execute(); // Remove from dataBase
        } else {
            Log.e(TAG, "View not found to remove !");
        }
    }

    private ImageView addAirplaneToView(ImageView aImageView, AirplaneData aAirplaneData) {
        return addAirplaneToView(aImageView, Airplane.loadFromDatabase(aAirplaneData));
    }

    private ImageView addAirplaneToView(ImageView aImageView, Airplane aAirplane) {
        aImageView.setImageBitmap(mAirplaneView.getBitmap());
        aImageView.setX(mGraphView.interpX(aAirplane.getCoordinateX()));
        aImageView.setY(mGraphView.interpY(aAirplane.getCoordinateY()));
        aImageView.setRotation(aAirplane.getDirection());
        aImageView.setTag(aAirplane);
        aImageView.setOnTouchListener(MainActivity.this);
        return aImageView;
    }

}
