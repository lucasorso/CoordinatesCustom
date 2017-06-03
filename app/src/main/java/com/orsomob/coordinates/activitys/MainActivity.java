package com.orsomob.coordinates.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.Toast;

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.module.Airplane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements InsertFragment.AirplaneListener, View.OnTouchListener {

    private static final int EDIT = 10;
    private static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private Realm mRealm;
    private List<Airplane> mAirplaneList;
    private List<ImageView> mViewList;
    private View.OnClickListener mSnackOnlick;

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
                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_rotation:
                startActivity(new Intent(this, RotationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onReceiveAirplane(Airplane aAirplane) {
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

        mViewList.add(lImageView);
        aAirplane.setName(aAirplane.getName() + mAirplaneList.size() + 1);
        mAirplaneList.add(aAirplane);

        Snackbar lSnackbar = Snackbar
                .make(this.getRootLayout().findFocus(), "Airplane added !", Snackbar.LENGTH_LONG)
                .setAction("UNDO", mSnackOnlick)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        lSnackbar.show();

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
        mViewList = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();
        getReferences();
        setEvents();

        funTest(); // TODO: 6/3/17 REMOVER
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

        mGraphView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.addView(lImageView);
            }
        }, 2300);

        mViewList.add(lImageView);
    }

    private void getReferences() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);
    }

    private void setEvents() {
        mSnackOnlick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*Nothing*/
            }
        };
    }

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
        lBundle.putSerializable("airplane", (Serializable) aAirplane);
        lIntent.putExtras(lBundle);
        startActivityForResult(lIntent, EDIT);
    }
}
