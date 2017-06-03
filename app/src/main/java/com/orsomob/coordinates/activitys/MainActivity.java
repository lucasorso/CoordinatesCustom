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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orsomob.coordinates.AirplaneListener;
import com.orsomob.coordinates.GraphView;
import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements AirplaneListener, View.OnTouchListener{

    public static final int HISTORY = 20;
    private static final String TAG = "MAIN_ACTIVITY";

    private RelativeLayout mRelativeLayout;
    private GraphView mGraphView;
    private Realm mRealm;
    private List<Airplane> mAirplaneList;
    private List<ImageView> mViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mAirplaneList = new ArrayList<>();
        mViewList = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();
        getReferences();
    }

    private void getReferences() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mGraphView = (GraphView) findViewById(R.id.gf_main);
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
        aAirplane.setName(aAirplane.getName()+ mAirplaneList.size() +1);
        mAirplaneList.add(aAirplane);

        Snackbar lSnackbar = Snackbar
                .make(this.getRootLayout().findFocus(), "Airplane added !", Snackbar.LENGTH_LONG)
                .setAction("UNDO", snackOnlick)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        lSnackbar.show();

    }

    @Override
    public void onUpdateAirplane(Airplane aAirplane) {
        /*Nothing*/
    }

    /**
     * Remove last item added
     */
    View.OnClickListener snackOnlick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Nothing*/
        }
    };

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        try {
            Airplane lAirplane = (Airplane) v.getTag();
            Snackbar lSnackbar = Snackbar.make(this.getRootLayout().findFocus(), lAirplane.getName(), Snackbar.LENGTH_LONG);
            lSnackbar.show();
        } catch (ClassCastException aE) {
            aE.printStackTrace();
        }
        return false;
    }
}
