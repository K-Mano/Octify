package org.ravenest.octify.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CircleChartView extends LinearLayout {

    private float INNER_RADIUS = 200;
    private float OUTER_RADIUS = 250;

    private float VALUE = 65;

    private float posX;
    private float posY;

    private int color = Color.argb(255,0,0,255);

    public CircleChartView(@NonNull Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public CircleChartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public CircleChartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public CircleChartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
    }

    public void init(){
        this.posX = this.getX() + this.getWidth() / 2;
        this.posY = this.getY() + this.getHeight() / 2;
    }

    public void setPosition(float posX, float posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void setScale(float innerR, float outerR){
        INNER_RADIUS = innerR;
        OUTER_RADIUS = outerR;
    }

    public void setValue(float percent){
        VALUE = percent;
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        init();
        Paint base = new Paint();
        base.setAntiAlias(true);
        base.setStrokeWidth(1);
        base.setColor(Color.argb(20,100,100,100));
        base.setStyle(Paint.Style.FILL);

        Paint draw = new Paint();
        draw.setAntiAlias(true);
        draw.setStrokeWidth(1);
        draw.setColor(color);
        draw.setStyle(Paint.Style.FILL);
        // ベースを描画
        canvas.drawPath(drawRing(posX,posY,OUTER_RADIUS ,0,180, 0.8f),base);
        canvas.drawPath(drawRing(posX,posY,OUTER_RADIUS ,0,-180, 0.8f),base);

        canvas.drawPath(drawRing(posX,posY,OUTER_RADIUS ,-90, (VALUE*360/100),0.8f),draw);
        this.invalidate();
    }

    public Path drawRing(float x, float y, float radius, float startAngle, float sweepAngle, float holeProportion) {
        Path donutPath = new Path();
        donutPath.moveTo(
                x + (float) (radius*Math.cos(Math.toRadians(startAngle))),
                y + (float) (radius*Math.sin(Math.toRadians(startAngle)))
        );
        donutPath.arcTo(
                new RectF(x-radius, y-radius, x+radius, y+radius),
                startAngle,
                sweepAngle
        );
        donutPath.lineTo(
                x + (float) (radius*holeProportion*Math.cos(Math.toRadians(startAngle+sweepAngle))),
                y + (float) (radius*holeProportion*Math.sin(Math.toRadians(startAngle+sweepAngle)))
        );
        donutPath.arcTo(
                new RectF(x-radius*holeProportion, y-radius*holeProportion, x+radius*holeProportion, y+radius*holeProportion),
                startAngle+sweepAngle,
                -sweepAngle
        );
        donutPath.close();
        return donutPath;
    }
}
