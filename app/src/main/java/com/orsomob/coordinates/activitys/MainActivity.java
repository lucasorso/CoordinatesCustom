package com.orsomob.coordinates.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.orsomob.coordinates.data.module.AirplaneData;
import com.orsomob.coordinates.data.module.AirplaneData_Table;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.module.Airplane;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements InsertFragment.AirplaneListener, View.OnTouchListener {

    private static final int EDIT = 10;
    private static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private List<Airplane> mAirplaneList;

    /**
     * Override methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                startActivity(new Intent(this, HistoryActivity.class));
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

        final ImageView lImageView = new ImageView(this);

        lImageView.setImageBitmap(getBitmap());
        lImageView.setX(mGraphView.interpX(aAirplane.getCoordinateX()));
        lImageView.setY(mGraphView.interpY(aAirplane.getCoordinateY()));
        lImageView.setRotation(aAirplane.getDirection());
        lImageView.setTag(aAirplane);
        lImageView.setOnTouchListener(this);

        mGraphView.post(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.addView(lImageView);
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

    /**
     * Non override methods
     */

    private void init() {
        mAirplaneList = new ArrayList<>();
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
                        final ImageView lImageView = new ImageView(MainActivity.this);
                        Airplane lAirplane = Airplane.loadFromDatabase(aAirplaneData);

                        lImageView.setImageBitmap(getBitmap());
                        lImageView.setX(mGraphView.interpX(lAirplane.getCoordinateX()));
                        lImageView.setY(mGraphView.interpY(lAirplane.getCoordinateY()));
                        lImageView.setRotation(lAirplane.getDirection());
                        lImageView.setTag(lAirplane);
                        lImageView.setOnTouchListener(MainActivity.this);

                        mGraphView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRelativeLayout.addView(lImageView);
                            }
                        });
                    }
                }
            }
        }, 1000);
    }

    private void funTest() {
        final ImageView lImageView = new ImageView(this);
        Airplane lAirplane = new Airplane();

        lAirplane.setCoordinateX(5.0f);
        lAirplane.setCoordinateY(-5.0f);
        lAirplane.setDegree(7.0f);
        lAirplane.setDirection(45.0f);
        lAirplane.setRadius(0.0f);
        lAirplane.setSpeed(1000.0f);
        lAirplane.setName("Airplane Teste");

        lImageView.setImageBitmap(getBitmap());
        lImageView.setX(mGraphView.interpX(lAirplane.getCoordinateX()));
        lImageView.setY(mGraphView.interpY(lAirplane.getCoordinateY()));
        lImageView.setRotation(lAirplane.getDirection());
        lImageView.setTag(lAirplane);
        lImageView.setOnTouchListener(this);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mGraphView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRelativeLayout.addView(lImageView);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 30000);
    }

    private void getReferences() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);
    }

    private void setEvents() {/*Nothing*/}

    public RelativeLayout getRootLayout() {
        return mRelativeLayout;
    }

    public Bitmap getBitmap() {
        return decodeSampledBitmapFromResource(this.getResources(), R.drawable.airplane_top, 15, 15);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
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

    private void editAirplane(Airplane aAirplane) {
        // TODO: 6/8/17 Fazer editar avião
    }

    private void removeAirplane(Airplane aAirplane) {
        Airplane lAirplane;
        final int childCount = mRelativeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View lChildAt = mRelativeLayout.getChildAt(i);
            if (lChildAt instanceof ImageView) {
                lAirplane = (Airplane) lChildAt.getTag();
                if (Objects.equals(lAirplane.getId(), aAirplane.getId())) {
                    mRelativeLayout.removeView(lChildAt); // Remove from screen
                    SQLite.delete().from(AirplaneData.class).where(AirplaneData_Table.id.is(aAirplane.getId())).execute(); // Remove from dataBase
                }
            }
        }
    }
}
