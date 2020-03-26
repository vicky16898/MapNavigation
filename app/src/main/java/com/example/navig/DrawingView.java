package com.example.navig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View implements Runnable {
    public int height;
    public int width;
    public float density;
    public boolean isTouch = false;
    Bitmap bitmap = null;
    int scaleX = 0, scaleY = 0;
    int finScaleX = 0; int finScaleY = 0;
    float scaleFactor = 1f;
    float dSCF = 0.2f;

    public DrawingView(Context context, int height, int width, float density) {
        super(context);
        this.height = height;
        this.width = width;
        this.density = density;
        init();
    }

    public void init()
    {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(width, height, conf);
        Canvas canvas = new Canvas(bitmap);
        Bitmap map = ((BitmapDrawable)getResources().getDrawable(R.drawable.map)).getBitmap();
        map = Bitmap.createScaledBitmap(map, (int)(500f*density), (int)(500f*density), false);
        canvas.drawBitmap(map, new Rect(0, 0, map.getWidth(), map.getHeight()), new Rect(0, 0, width, height) , null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("DENSITY", String.valueOf(density));
        if(!isTouch)
        {
            canvas.save();
            canvas.scale(scaleFactor, scaleFactor, finScaleX, finScaleY);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.restore();
        }
        else
        {
            canvas.save();
            canvas.scale(scaleFactor, scaleFactor, scaleX, scaleY);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            int X = (int) event.getX();
            int Y = (int) event.getY();
            int eventAction = event.getAction();
            switch (eventAction)
            {
                case MotionEvent.ACTION_DOWN:
                    isTouch = !isTouch;
                    scaleX = X;
                    scaleY = Y;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return super.onTouchEvent(event);
    }

    @Override
    public void run() {
        while(!Thread.interrupted())
        {
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isTouch)
            {   finScaleX = scaleX;
                finScaleY = scaleY;
                if(scaleFactor < 5f)
                scaleFactor = scaleFactor + dSCF;
            }
            else
            {
                if(scaleFactor <= 1f)
                    scaleFactor = 1f;
                else
                    scaleFactor = scaleFactor - dSCF;
            }
            invalidate();
        }
    }
}
