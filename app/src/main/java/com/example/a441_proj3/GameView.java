package com.example.a441_proj3;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

//    GameThread gameThread;

    Handler handler;
    Runnable runnable;
    final int UPDATE_MS = 30;
    Bitmap background;
    Bitmap toptube, bottomtube;
    Display display;
    Point point;
    int dWidth, dHeight;
    Rect rect;

    Bitmap[] orbs;
    int orbFrame = 0;
    int velocity = 0;
    int gravity = 3;
    int orbX, orbY;
    boolean gameState = false;
    int gap = 400;
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random rand;
    int tubeVelocity = 8;

    public GameView(Context context){
        super(context);
//        initView();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        toptube = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle);
        bottomtube = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle2);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        orbs = new Bitmap[1];
        orbs[0] = BitmapFactory.decodeResource(getResources(), R.drawable.orb);
//        orbs[1] = BitmapFactory.decodeResource(getResources(), R.drawable.orb2);
        orbX = dWidth/2 - orbs[0].getWidth()/2;
        orbY = dHeight/2-orbs[0].getHeight()/2;
        distanceBetweenTubes = dWidth*3/4;
        minTubeOffset = gap/2;
        maxTubeOffset = dHeight - minTubeOffset - gap;
        rand = new Random();
        for(int i = 0; i < numberOfTubes; i++){
            tubeX[i] = dWidth + i*distanceBetweenTubes;
            topTubeY[i] = minTubeOffset + rand.nextInt(maxTubeOffset - minTubeOffset + 1);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);
//        if(orbFrame == 0){
//            orbFrame = 1;
//        } else {
//            orbFrame = 0;
//        }
        if(gameState){
            if(orbY < dHeight - orbs[0].getHeight() || velocity < 0){
                velocity += gravity;
                orbY += velocity;
            }
            for(int i = 0; i < numberOfTubes; i++){
                tubeX[i] -= tubeVelocity;
                if(tubeX[i] < -toptube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + rand.nextInt(maxTubeOffset - minTubeOffset + 1);
                }
                canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(),null);
                canvas.drawBitmap(bottomtube,tubeX[i],topTubeY[i] + gap, null);
            }

        }

        canvas.drawBitmap(orbs[orbFrame],orbX, orbY,null);
        handler.postDelayed(runnable,UPDATE_MS);
    }


    @Override
    public boolean onTouchEvent (MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            velocity = -30;
            gameState = true;

        }

        return true;
    }

    /*void initView(){
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        gameThread = new GameThread(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!gameThread.isRunning()){
            gameThread = new GameThread(holder);
            gameThread.start();
        } else{
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameThread.isRunning()){
            gameThread.setIsRunning(false);
            boolean retry = true;
            while(retry){
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e){
                    Log.e("Interrupted","Interrupted while joining");
                }
            }
        }
    }
*/
}