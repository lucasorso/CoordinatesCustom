package com.orsomob.coordinates;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int INSERT_REMOVE = 10;

    RelativeLayout mRelativeLayoutMain;
    Display display;

    private GraphView mGraphView;
//    private Canvas mCanvas;
//    private com.jjoe64.graphview.GraphView mGraphView;
//    private ScatterChart mGraphView;
//    private CanvasView mGraphView;

//    private List<Entry> mEntryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayoutMain = (RelativeLayout) findViewById(R.id.rl_main);
//        mGraphView = (ScatterChart) findViewById(R.id.graphView);
//        mGraphView = (com.jjoe64.graphview.GraphView) findViewById(R.id.graphView);
//        mGraphView = (ScatterChart) findViewById(R.id.graphView);
        mGraphView = (GraphView) findViewById(R.id.graphView);


        /*LimitLine lLine = new LimitLine(0f);
        lLine.setLineColor(R.color.colorAccent);
        lLine.setLineWidth(2f);

        XAxis xAxis = mGraphView.getXAxis();
        xAxis.setAxisMinimum(-20f);
        xAxis.setAxisMaximum(20f);
        xAxis.addLimitLine(lLine);

        YAxis yAxisLeft = mGraphView.getAxisLeft();
        yAxisLeft.setAxisMinimum(-20f);
        yAxisLeft.setAxisMaximum(20f);
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.addLimitLine(lLine);

        mGraphView.getAxisRight().setEnabled(false);
        mGraphView.getXAxis().setDrawGridLines(false);
        mGraphView.getXAxis().setDrawAxisLine(true);
        mGraphView.getXAxis().setDrawLimitLinesBehindData(true);
        mGraphView.getAxisLeft().setDrawGridLines(false);


        Bitmap lBitmap = getBitmap();
        MarkerImage lMarkerImage = new MarkerImage(this, R.drawable.airplane_top);
        lMarkerImage.setOffset(-lBitmap.getWidth() /2, -lBitmap.getHeight() / 2);

        mEntryList.add(new Entry(6f, 7));
        mEntryList.add(new Entry(1f, 6));

        ScatterDataSet lScatterDataSet = new ScatterDataSet(mEntryList, "Lucas");

        ScatterData lScatterData = new ScatterData(lScatterDataSet);

        mGraphView.setMarker(lMarkerImage);
        mGraphView.setData(lScatterData);*/

        display = getWindowManager().getDefaultDisplay();

        ImageView lImageView = new ImageView(this);
        lImageView.setImageBitmap(getBitmap());
        int xp = 4;
        int yp = -4;

        lImageView.setX(interpX(xp) -(getBitmap().getWidth() / 2));
        lImageView.setY(interpY(yp) -(getBitmap().getHeight() / 2));

//        AbsoluteLayout.LayoutParams param = new AbsoluteLayout.LayoutParams(int width, int height, int x, int y)
//        canvas.drawBitmap(lBitmap, interpX(xp) - (lBitmap.getWidth() / 2), interpY(yp) - (lBitmap.getHeight() / 2), new Paint(Paint.FILTER_BITMAP_FLAG));

        // Setting layout params to our RelativeLayout

        // Finally Adding the imageView to RelativeLayout and its position
        mRelativeLayoutMain.addView(lImageView);


        lImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Airplane", Toast.LENGTH_SHORT).show();
            }
        });


    }

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
            case R.id.menu_inserir:
                Toast.makeText(this, "Inserir", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(this, InsertRemove.class), INSERT_REMOVE);
                break;
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
        switch (requestCode) {
            case INSERT_REMOVE:
//                Bundle lBundle = data.getExtras();
                Toast.makeText(this, "VOLTOO DO INSERT REMOVE", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private Bitmap getBitmap() {
        /*BitmapFactory.Options lOptions = new BitmapFactory.Options();
        lOptions.inMutable = false;
        return BitmapFactory.decodeResource(this.getResources(),R.drawable.airplane_top, lOptions);*/
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

    private float interpX(double x) {
        double width = (double) display.getWidth();
        return (float) ((x + this.mGraphView.getGridDimension()) / (this.mGraphView.getGridDimension() * 2) * width);
    }

    private float interpY(double y) {
        double height = (double) display.getHeight();
        return (float) ((y + mGraphView.getGridDimension()) / (this.mGraphView.getGridDimension() * 2) * -height + height);
    }

}
