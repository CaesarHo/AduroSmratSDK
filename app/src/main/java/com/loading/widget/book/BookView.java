package com.loading.widget.book;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.adurosmart.sdk.R;

/**
 * The background
 * Created by best on 15/7/28.
 */
public class BookView extends View {

    private Paint paint;
    private int width;
    private int height;

    public BookView(Context context) {
        super(context);
        initView();
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(getResources().getDimension(R.dimen.activity_horizontal_margin));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, width, height, paint);
    }
}
