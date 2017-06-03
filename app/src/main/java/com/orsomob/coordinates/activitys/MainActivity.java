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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.RotationActivity;
import com.orsomob.coordinates.fragments.InsertFragment;
import com.orsomob.coordinates.module.Airplane;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements InsertFragment.AirplaneListener {

    public static final int HISTORY = 20;
    private static final String TAG = "MainActivity";

    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

//        final ImageView lImageView = new ImageView(this);

//        lImageView.setImageBitmap(getBitmap());

//        final int xp = 4;
//        final int yp = -4;

//        Log.i(TAG, "interpX : " + interpX(xp));
//        Log.i(TAG, "interpY : " + interpY(yp));

//        Log.i(TAG, "interpX GraphView : " + mGraphView.interpX(xp));
//        Log.i(TAG, "interpY GraphView : " + mGraphView.interpY(yp));

//        lImageView.setX(interpX(xp) -(getBitmap().getWidth() / 2));
//        lImageView.setY(interpY(yp) -(getBitmap().getHeight() / 2));

//        mRelativeLayout.addView(lImageView);

//        mGraphView.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG, "interpX GraphView : " + mGraphView.interpX(xp));
//                Log.i(TAG, "interpY GraphView : " + mGraphView.interpY(yp));
//                lImageView.setX(mGraphView.interpX(xp));
//                lImageView.setY(mGraphView.interpY(yp));
//                mRelativeLayout.addView(lImageView);
//            }
//        });
//
//        lImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Airplane", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void init() {
        mRealm = Realm.getDefaultInstance();
        getReferences();
    }

    private void getReferences() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);

    }

    public GraphView getGraphView() {
        return mGraphView;
    }

    public RelativeLayout getRootLayout() {
        return mRelativeLayout;
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
                Toast.makeText(this, "Historico", Toast.LENGTH_SHORT).show();
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
        lImageView.setX(mGraphView.interpX(4f));
        lImageView.setY(mGraphView.interpY(-3f));
        mGraphView.post(new Runnable() {
            @Override
            public void run() {
                lImageView.setRotation(90f);
                mRelativeLayout.addView(lImageView);
            }
        });

        Snackbar lSnackbar = Snackbar
                .make(this.getCurrentFocus(), "Airplane added !", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        lSnackbar.show();

    }


    private Bitmap getBitmap() {
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
}
