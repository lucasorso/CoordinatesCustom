package com.orsomob.coordinates.activitys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.ViewPagerAdapter;
import com.orsomob.coordinates.animations.ZoomOutPageTransformer;
import com.orsomob.coordinates.db.AirplaneDB;
import com.orsomob.coordinates.db.AirplaneDB_Table;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.fragments.InsertFragment.AirplaneListener;
import com.orsomob.coordinates.fragments.RotationFragment;
import com.orsomob.coordinates.fragments.TranslationFragment;
import com.orsomob.coordinates.interfaces.AirplaneTouch;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;
import com.orsomob.coordinates.property.Option;
import com.orsomob.coordinates.util.AirplaneView;
import com.orsomob.coordinates.util.Function;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AirplaneListener, RotationFragment.AirplaneRotate, TranslationFragment.AirplaneTranslate, View.OnTouchListener {

    private final int FRAGMENT_INSERT = 0;
    private final int FRAGMENT_ROTATION = 1;
    private final int FRAGMENT_TRANSLATION = 2;

    private static final int EDIT = 10;
    private static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private int mSelectedFragment;
    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private ViewPager mViewPager;
    private AirplaneView mAirplaneView;
    private AirplaneTouch mAirplaneTouch;

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
                        int lOption = lBundle.getInt("option");
                        Airplane lAirplane = lBundle.containsKey("airplane") ? (Airplane) lBundle.get("airplane") : null;
                        switch (lOption) {
                            case Option.EDIT_AIRPLANE:
                                editAirplane(lAirplane);
                                break;
                            case Option.REMOVE_AIRPLANE:
                                removeAirplane(lAirplane);
                                break;
                        }

                    }
                }
            }
            break;
            case HISTORY:
                // TODO: 6/7/17 FAZER - Doing
                if (RESULT_OK == resultCode) {
                    Bundle lBundle = data.getExtras();
                    if (lBundle != null && lBundle.containsKey("option")) {
                        int lOption = lBundle.getInt("option");
                        switch (lOption) {
                            case Option.REMOVE_AIRPLANE:
                                List<Airplane> lAirplanes = (List<Airplane>) lBundle.getSerializable("list");
                                if (lAirplanes != null && !lAirplanes.isEmpty()) {
                                    for (Airplane lPlane : lAirplanes) {
                                        removeAirplane(lPlane);
                                    }
                                } else {
                                    Toast.makeText(this, "Empty List !", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onReceiveAirplane(final Airplane aAirplane) {

        //Set ramdom name to Airplane
        String[] lAirplaneNames = getResources().getStringArray(R.array.airplane_select);
        Random lRandom = new Random();
        String lName = lAirplaneNames[lRandom.nextInt(lAirplaneNames.length)];
        aAirplane.setName(lName);

        int id = Airplane.saveToDatabase(aAirplane).getId();
        aAirplane.setId(id);

        editAirplane(aAirplane);

        Airplane lAirplane = Airplane.loadAirplaneFromDataBase(aAirplane);

        ImageView lImageView = new ImageView(this);
        lImageView = addAirplaneToView(lImageView, lAirplane);
        final ImageView finalLImageView = lImageView;
        mGraphView.post(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.addView(finalLImageView);
            }
        });

        showMessaUndo(lAirplane);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            Airplane lAirplane = (Airplane) v.getTag();

            switch (mSelectedFragment) {
                case FRAGMENT_INSERT:
                    openEdit(lAirplane);
                    break;
                case FRAGMENT_ROTATION:
                    mAirplaneTouch.onAirplaneTouch(lAirplane);
                    break;
                case FRAGMENT_TRANSLATION:
                    mAirplaneTouch.onAirplaneTouch(lAirplane);
                    break;
            }
        } catch (ClassCastException aE) {
            Log.e(TAG, "onTouch: Couldn`t catch airplane");
            aE.printStackTrace();
        }
        return false;
    }

    @Override
    public void onRotateAirplane(Float aX, Float aY, final Float aDregree, Airplane aAirplane, PointDouble lNewPoint) {
        ImageView lImageView = getAirplaneFromView(aAirplane);


        Log.i(TAG, "---- Point on Rotate X, Y ----");
        Log.i(TAG, "rotate X " + aX);
        Log.i(TAG, "rotate Y " + aY);
        Log.i(TAG, "rotate X Absolute " + mGraphView.interpX(aX));
        Log.i(TAG, "rotate Y Absolute " + mGraphView.interpX(aY));

        Log.i(TAG, "---- Point Airplane X, Y ----");
        Log.i(TAG, "Airplane X" + aAirplane.getCoordinateX());
        Log.i(TAG, "Airplane Y" + aAirplane.getCoordinateY());
        Log.i(TAG, "Airplane X Absolue " + mGraphView.interpX(aAirplane.getCoordinateX()));
        Log.i(TAG, "Airplane Y Absolue " + mGraphView.interpY(aAirplane.getCoordinateY()));


        RotateAnimation lRotateAnimation = new RotateAnimation(0, aDregree, mGraphView.interpX(aX), mGraphView.interpY(aY));
        lRotateAnimation.setInterpolator(new LinearInterpolator());
        lRotateAnimation.setDuration(3000);
        lRotateAnimation.setFillAfter(true);
        lImageView.startAnimation(lRotateAnimation);

        Log.i(TAG, "---- New Point X, Y ----");
        Log.i(TAG, "new Point X " + lNewPoint.getX());
        Log.i(TAG, "new Point Y " + lNewPoint.getY());
        Log.i(TAG, "new Point X Absolute " + mGraphView.interpX(lNewPoint.getX()));
        Log.i(TAG, "new Point Y Absolute " + mGraphView.interpY(lNewPoint.getY()));

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
//        funTest(); // DONE: 6/3/17 REMOVER
    }

    private void getFromDataBase() {

        final List<AirplaneDB> lAirplaneDBs = SQLite.select().from(AirplaneDB.class).queryList();

        Handler lHandler = new Handler();

        lHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!lAirplaneDBs.isEmpty()) {

                    for (AirplaneDB lAAirplaneDB : lAirplaneDBs) {
                        ImageView lImageView = new ImageView(MainActivity.this);
                        lImageView = addAirplaneToView(lImageView, lAAirplaneDB);
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
        lAirplane.setTheta(7.0f);
        lAirplane.setDirection(45.0f);
        lAirplane.setRadius(0.0f);
        lAirplane.setSpeed(1000.0f);
        lAirplane.setName("Airplane Teste");

        lImageView = addAirplaneToView(lImageView, lAirplane);
        final ImageView finalLImageView = lImageView;
        Handler mHandler = new Handler();
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
        mAirplaneView = new AirplaneView(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
    }

    private void setEvents() {
        configureViewPager();
    }

    private void configureViewPager() {

        ViewPagerAdapter lViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        lViewPagerAdapter.addFrag(new InsertFragment());
        lViewPagerAdapter.addFrag(new RotationFragment());
        lViewPagerAdapter.addFrag(new TranslationFragment());

        mViewPager.setAdapter(lViewPagerAdapter);
        mViewPager.offsetLeftAndRight(0);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setFitsSystemWindows(true);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*Nothing*/}

            @Override
            public void onPageSelected(int position) {
                mSelectedFragment = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*Nothing*/}
        });
//        configureFrgmentsListeners(lViewPagerAdapter);
    }

    public void setListenerFragment(AirplaneTouch aAirplaneTouch) {
        mAirplaneTouch = aAirplaneTouch;
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
                .make(mRelativeLayout.findFocus(), "Airplane added !", Snackbar.LENGTH_LONG)
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

            AnimatorSet animSetXY = new AnimatorSet();
            ObjectAnimator lCoordX = ObjectAnimator.ofFloat(lView, "translationX", lView.getX(), mGraphView.interpX(aAirplane.getCoordinateX()));
            ObjectAnimator lCoordY = ObjectAnimator.ofFloat(lView, "translationY", lView.getY(), mGraphView.interpY(aAirplane.getCoordinateY()));

            animSetXY.playTogether(lCoordX, lCoordY);
            animSetXY.setInterpolator(new LinearInterpolator());
            animSetXY.setDuration(3000);
            animSetXY.start();

            lView.setTag(aAirplane);
            lView.setOnTouchListener(MainActivity.this);
            lView.setRotation(aAirplane.getDirection());

            updateDataBase(aAirplane);

        } else {
            Log.e(TAG, "View not found to edit !");
        }
    }

    private void removeAirplane(Airplane aAirplane) {
        View lView = getAirplaneFromView(aAirplane);
        if (lView != null) {
            mRelativeLayout.removeView(lView); // Remove from screen
            SQLite.delete().from(AirplaneDB.class).where(AirplaneDB_Table.id.is(aAirplane.getId())).execute(); // Remove from dataBase
        } else {
            Log.e(TAG, "View not found to remove !");
        }
    }

    private ImageView addAirplaneToView(ImageView aImageView, AirplaneDB aAirplaneDB) {
        return addAirplaneToView(aImageView, Airplane.loadFromDatabase(aAirplaneDB));
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

    private void updateDataBase(Airplane aAirplane) {
        //update theta and Radius through nem coordinates
        PointDouble lNewDRadiustheta = Function.convertCartesianToPolar(aAirplane.getCoordinateX(), aAirplane.getCoordinateY());

        AirplaneDB lAirplaneDB = Airplane.loadFromDataBase(aAirplane);
        lAirplaneDB.setName(aAirplane.getName());
        lAirplaneDB.setCoordinateX(Double.valueOf(aAirplane.getCoordinateX()));
        lAirplaneDB.setCoordinateY(Double.valueOf(aAirplane.getCoordinateY()));
        lAirplaneDB.setTheta(lNewDRadiustheta.y);
        lAirplaneDB.setDirection(Double.valueOf(aAirplane.getDirection()));
        lAirplaneDB.setRadius(lNewDRadiustheta.x);
        lAirplaneDB.setSpeed(Double.valueOf(aAirplane.getSpeed()));
        lAirplaneDB.update(); // UpdateDabase
    }

}
