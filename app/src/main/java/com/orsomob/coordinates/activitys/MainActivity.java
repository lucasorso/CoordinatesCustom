package com.orsomob.coordinates.activitys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
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

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.adapter.ViewPagerAdapter;
import com.orsomob.coordinates.animations.ZoomOutPageTransformer;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.data.module.AirplaneData_Table;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.fragments.InsertFragment.AirplaneListener;
import com.orsomob.coordinates.fragments.RotationFragment;
import com.orsomob.coordinates.fragments.TranslationFragment;
import com.orsomob.coordinates.interfaces.AirplaneTouch;
import com.orsomob.coordinates.module.Airplane;
import com.orsomob.coordinates.module.PointDouble;
import com.orsomob.coordinates.util.AirplaneView;
import com.orsomob.coordinates.util.Function;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AirplaneListener, RotationFragment.AirplaneRotate, TranslationFragment.AirplaneTranslate, View.OnTouchListener{

    private final int FRAGMENT_INSERT = 0;
    private final int FRAGMENT_ROTATION = 1;
    private final int FRAGMENT_TRANSLATION = 2;

    private static final int EDIT = 10;
    private static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private int mSelectedFragment;
    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private List<Airplane> mAirplaneList;
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

    /*public void onTouchAirplane(AirplaneTouch aAirplaneTouch) {
        mAirplaneTouch = aAirplaneTouch;
    }*/

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

        RotateAnimation lRotateAnimation = new RotateAnimation(aAirplane.getDegree(), aDregree, mGraphView.interpX(aX), mGraphView.interpY(aY));
        lRotateAnimation.setInterpolator(new LinearInterpolator());
        lRotateAnimation.setDuration(3000);
        lImageView.startAnimation(lRotateAnimation);

        Log.i(TAG, "---- New Point X, Y ----");
        Log.i(TAG, "new Point X " + lNewPoint.getX());
        Log.i(TAG, "new Point Y " + lNewPoint.getY());
        Log.i(TAG, "new Point X Absolute " + mGraphView.interpX(lNewPoint.getX()));
        Log.i(TAG, "new Point Y Absolute " + mGraphView.interpY(lNewPoint.getY()));


//        AnimatorSet animSetXY = new AnimatorSet();
//        ObjectAnimator lCoordX = ObjectAnimator.ofFloat(lImageView, "translationX", mGraphView.interpX(aAirplane.getCoordinateX()), lImageView.getX());
//        ObjectAnimator lCoordY = ObjectAnimator.ofFloat(lImageView, "translationY", mGraphView.interpY(aAirplane.getCoordinateY()), lImageView.getY());
//        animSetXY.playTogether(lCoordX,lCoordY);

//        Log.i(TAG, "new X with static image " + lImageView.getX());
//        Log.i(TAG, "new Y with static image " + lImageView.getY());


        /*int[] locationOnScreent = new int[2];
        lImageView.getLocationOnScreen(locationOnScreent);

        Log.i(TAG, "new X with getLocationOnScreen " + locationOnScreent[0]);
        Log.i(TAG, "new Y with getLocationOnScreen " + locationOnScreent[1]);
        Log.i(TAG, "Rotation " + lImageView.getRotation());*/


        /*lRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setFillAfter(true);
                animation.setFillEnabled(true);

                Log.i(TAG, "OnAnimation End - new X with static image " + lImageView.getX());
                Log.i(TAG, "OnAnimation End - new Y with static image " + lImageView.getY());


                int[] locationOnScreent = new int[2];
                lImageView.getLocationOnScreen(locationOnScreent);

                Log.i(TAG, "OnAnimation End - new X with getLocationOnScreen " + locationOnScreent[0]);
                Log.i(TAG, "OnAnimation End - new Y with getLocationOnScreen " + locationOnScreent[1]);
                Log.i(TAG, "OnAnimation End - Rotation " + lImageView.getRotation());

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });*/
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
                        lImageView = addAirplaneToView(lImageView, aAirplaneData);
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

    private void updateDataBase(Airplane aAirplane) {
        //update Degree and Radius through nem coordinates
        Point lNewDRadiusDegree = Function.convertCartesianToPolar(aAirplane.getCoordinateX(), aAirplane.getCoordinateY());

        AirplaneData lAirplaneData = Airplane.loadFromDataBase(aAirplane);
        lAirplaneData.setName(aAirplane.getName());
        lAirplaneData.setCoordinateX(Double.valueOf(aAirplane.getCoordinateX()));
        lAirplaneData.setCoordinateY(Double.valueOf(aAirplane.getCoordinateY()));
        lAirplaneData.setDegree((double) lNewDRadiusDegree.y);
        lAirplaneData.setDirection(Double.valueOf(aAirplane.getDirection()));
        lAirplaneData.setRadius((double) lNewDRadiusDegree.x);
        lAirplaneData.setSpeed(Double.valueOf(aAirplane.getSpeed()));
        lAirplaneData.update(); // UpdateDabase
    }

}
