package com.adurosmart.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.adurosmart.sdk.R;


/**
 * Created by best on 2016/12/2.
 */

public class ColorPickerView  extends View {
    private Context mContext;
    private Paint mRightPaint;
    private int mHeight;
    private int mWidth;
    private int[] mRightColors;
    private int LEFT_WIDTH;
    private Bitmap mLeftBitmap;
    private Bitmap mLeftBitmap2;
    private Paint mBitmapPaint;
    private PointF mLeftSelectPoint;
    private OnColorChangedListener mChangedListener;
    private boolean mLeftMove = false;
    private float mLeftBitmapRadius;
    private Bitmap mGradualChangeBitmap;
    private Bitmap bitmapTemp;
    private int newWidgth;
    private int newHeigh;
    public static int hue = 0;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setOnColorChangedListenner(OnColorChangedListener listener) {
        mChangedListener = listener;
    }

    private void init() {
        bitmapTemp = BitmapFactory.decodeResource(getResources(), R.mipmap.color_buttom);
        mRightPaint = new Paint();
        mRightPaint.setStyle(Paint.Style.FILL);
        mRightPaint.setStrokeWidth(1);
        mRightColors = new int[3];
        mRightColors[0] = Color.WHITE;
        mRightColors[2] = Color.BLACK;
        mBitmapPaint = new Paint();

        mLeftBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.reading__color_view__button);
        mLeftBitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.reading__color_view__button_press);
        mLeftBitmapRadius = mLeftBitmap.getWidth() / 2;
        mLeftSelectPoint = new PointF(0, 0);
        newWidgth = BitmapFactory.decodeResource(getResources(), R.mipmap.color_buttom).getWidth();
        newHeigh = BitmapFactory.decodeResource(getResources(), R.mipmap.color_buttom).getHeight();
    }

    //important patient please!!!
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(getGradual(), null, new Rect(0, 0, LEFT_WIDTH, mHeight), mBitmapPaint);

        if (mLeftMove) {
            canvas.drawBitmap(mLeftBitmap, mLeftSelectPoint.x - mLeftBitmapRadius,
                    mLeftSelectPoint.y - mLeftBitmapRadius, mBitmapPaint);
        } else {
            try {
                canvas.drawBitmap(mLeftBitmap2, mLeftSelectPoint.x - mLeftBitmapRadius,
                        mLeftSelectPoint.y - mLeftBitmapRadius, mBitmapPaint);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            mWidth = newHeigh;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            mHeight = newHeigh;
        }
        LEFT_WIDTH = mWidth;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mLeftMove = true;
                proofLeft(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                hue = getLeftColor(x, y);
                System.out.println("getLeftColor x = " + x + "," + "y = " + y);
                mLeftMove = false;
                invalidate();

                mChangedListener.onColorChanged(hue);
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mGradualChangeBitmap != null && mGradualChangeBitmap.isRecycled() == false) {
            mGradualChangeBitmap.recycle();
        }
        if (mLeftBitmap != null && mLeftBitmap.isRecycled() == false) {
            mLeftBitmap.recycle();
        }
        if (mLeftBitmap2 != null && mLeftBitmap2.isRecycled() == false) {
            mLeftBitmap2.recycle();
        }
        super.onDetachedFromWindow();
    }

    private Bitmap getGradual() {
        if (mGradualChangeBitmap == null) {
            Paint leftPaint = new Paint();
            leftPaint.setStrokeWidth(1);
            mGradualChangeBitmap = Bitmap.createBitmap(LEFT_WIDTH, mHeight, Bitmap.Config.RGB_565);
            mGradualChangeBitmap.eraseColor(Color.WHITE);
            Canvas canvas = new Canvas(mGradualChangeBitmap);
            canvas.drawBitmap(bitmapTemp, null, new Rect(0, 0, LEFT_WIDTH, mHeight), mBitmapPaint);
        }
        return mGradualChangeBitmap;
    }

    private void proofLeft(float x, float y) {
        if (x < 0) {
            mLeftSelectPoint.x = 0;
        } else if (x > (LEFT_WIDTH)) {
            mLeftSelectPoint.x = LEFT_WIDTH;
        } else {
            mLeftSelectPoint.x = x;
        }
        if (y < 0) {
            mLeftSelectPoint.y = 0;
        } else if (y > (mHeight - 0)) {
            mLeftSelectPoint.y = mHeight - 0;
        } else {
            mLeftSelectPoint.y = y;
        }
    }

    private int getLeftColor(float x, float y) {
        Bitmap temp = getGradual();
        int intX = (int) x;
        int intY = (int) y;
        if (intX < 0) intX = 0;
        if (intY < 0) intY = 0;
        if (intX >= temp.getWidth()) {
            intX = temp.getWidth() - 1;
        }
        if (intY >= temp.getHeight()) {
            intY = temp.getHeight() - 1;
        }
        System.out.println("getLeftColor x = " + intX + "," + "y = " + intY);
        return temp.getPixel(intX, intY);
    }

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }
}
