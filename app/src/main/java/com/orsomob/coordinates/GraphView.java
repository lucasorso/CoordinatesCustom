package com.orsomob.coordinates;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LucasOrso on 4/2/17.
 */

public class GraphView extends View {

    private static String TAG = "GraphView";

    // Stores graph and line information
    private int gridDimension;

    // Appearance fields
    private Paint gridPaint;
    private Paint axisPaint;
    private Paint textPaint;
    private Paint circlePaint;

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    // Initialize
    public void init() {
        // Set initial grid dimension
        setGridDimension(10);

        // Grid line paint
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        gridPaint.setColor(Color.GRAY);

        // Axis paint
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(3);
        axisPaint.setColor(Color.BLACK);

        // Text paint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(15);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(1);
        textPaint.setColor(Color.BLACK);

        // Circle paint
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);
    }

    // Interpolate from graph to Canvas coordinates
    public float interpX(double x) {
        double width = (double) this.getWidth();
        return (float) ((x + this.getGridDimension()) / (this.getGridDimension() * 2) * width);
    }

    public float interpY(double y) {
        double height = (double) this.getHeight();
        return (float) ((y + this.getGridDimension()) / (this.getGridDimension() * 2) * -height + height);
    }

    public int measureWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingLeft() - this.getPaddingRight();
    }

    public int measureHeight(int heightMeasureSpec) {
        return MeasureSpec.getSize(heightMeasureSpec);
    }

    public int getGridDimension() {
        return gridDimension;
    }

    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
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

    public Bitmap getBitmap() {
        return decodeSampledBitmapFromResource(this.getResources(), R.drawable.airplane_top, 15, 15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Background color
        canvas.drawColor(Color.WHITE);

        // Draw grid lines in x dimension (vertical lines)
        for (int x = -this.getGridDimension(); x <= this.getGridDimension(); ++x) {
            canvas.drawLine(interpX(x), interpY(this.getGridDimension()),
                    interpX(x), interpY(-this.getGridDimension()),
                    (x == 0) ? axisPaint : gridPaint);
        }

        // Draw grid lines in y dimension (horizontal lines)
        for (int y = -this.getGridDimension(); y <= this.getGridDimension(); ++y) {
            canvas.drawLine(interpX(-this.getGridDimension()), interpY(y),
                    interpX(this.getGridDimension()), interpY(y),
                    (y == 0) ? axisPaint : gridPaint);
        }

        // Draw coordinate text
        int step = this.getGridDimension() / gridDimension;

        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int x = -this.getGridDimension() + step; x < this.getGridDimension(); x += step) {
            if (x != 0) {
                canvas.drawText(Integer.toString(x), interpX(x), interpY(0), textPaint);
            }
        }

        textPaint.setTextAlign(Paint.Align.LEFT);
        for (int y = -this.getGridDimension() + step; y < this.getGridDimension(); y += step) {
            canvas.drawText(Integer.toString(y), interpX(0), interpY(y), textPaint);
        }

        // Compute start and end line coordinates
//        double x0 = -this.getGridDimension();
//        double y0 = solveLineEq(x0);
//        double x1 = this.getGridDimension();
//        double y1 = solveLineEq(x1);

        // Draw line
//        canvas.drawLine(interpX(x0), interpY(y0), interpX(x1), interpY(y1),linePaint);

        // Draw circle - Lucas Teste
//        double xp = 4.0;
//        double yp = -3.0;
//        canvas.drawCircle(interpX(xp), interpY(yp), this.getWidth() * (float) 0.01, circlePaint);
//        canvas.drawText("Airplane 1", interpX(xp) + 10, interpY(yp) + 10, textPaint);
//        Log.i(TAG, "interpX : " + interpX(xp));
//        Log.i(TAG, "interpY : " + interpY(yp));
//        Bitmap lBitmap = getBitmap();
//        canvas.drawBitmap(lBitmap, interpX(xp) - (lBitmap.getWidth() / 2), interpY(yp) - (lBitmap.getHeight() / 2), new Paint(Paint.FILTER_BITMAP_FLAG));
//        canvas.drawBitmap(getBitmap(), interpX(xp), interpY(yp), circlePaint);
        /*canvas.drawLine(interpX(xp), (this.getWidth() / 2) + (int) xp, interpY(yp),this.getHeight() / 2 + (int) yp, linePaint);*/
//        double xp = 4.0;
//        double yp = -3.0;
//        Bitmap lBitmap = getBitmap();
//        canvas.drawBitmap(lBitmap, interpX(xp) - (lBitmap.getWidth() / 2), interpY(yp) - (lBitmap.getHeight() / 2), null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

}
