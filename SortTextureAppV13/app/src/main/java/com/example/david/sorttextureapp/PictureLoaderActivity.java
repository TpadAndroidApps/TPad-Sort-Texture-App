package com.example.david.sorttextureapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.tpadlib.TPad;
import com.example.tpadlib.TPadImpl;
import com.example.tpadlib.views.FrictionMapView;

import java.util.ArrayList;

public class PictureLoaderActivity extends AppCompatActivity {
    private static final int INITIAL_HIDE_DELAY = 2000;
    private View mDecorView;

    // TPad object defined in TPadLib
    private FrictionMapView mFrictionMapView;
    private static TPad mTpad;

    int[] receivedOrder = new int[3]; // length should be the same as the total image
    int receiveCurrentImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_loader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.frictionView);

        // Data passed from ListArrangeActivity
        receiveCurrentImage = getIntent().getIntExtra("currentImage",0);
        ArrayList<String> data = getIntent().getStringArrayListExtra("passToFullActivity");
        for (int i = 0; i < data.size(); i++) {
            try {
                receivedOrder[i] = Integer.parseInt(data.get(i))-1;
            } catch (NumberFormatException nfe) {};
        }
        //Log.d("receive current", String.valueOf(receiveCurrentImage));

        mTpad = new TPadImpl(this);
        mFrictionMapView = (FrictionMapView) findViewById(R.id.frictionView);
        mFrictionMapView.setTpad(mTpad);

        // Reset the bitmap of the FrictionMapView with current user click image
        String path = ("sdcard/TPadImageRes/image"+receivedOrder[receiveCurrentImage]+".jpg");
        Bitmap mImage = BitmapFactory.decodeFile(path);
        if (mImage != null) {
            Log.d("TAG","mImage loaded");
            mFrictionMapView.setDataBitmap(mImage);
            mImage.recycle();
        }
        else {Log.d("TAG","mImage loaded FAIL");}

        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int flags) {
                        boolean visible = (flags & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        controlsView.animate()
                                .alpha(visible ? 1 : 0)
                                .translationY(visible ? 0 : controlsView.getHeight());
                    }
                });
        contentView.setClickable(true);
        final GestureDetector clickDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        boolean visible = (mDecorView.getSystemUiVisibility()
                                & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        if (visible) {
                            hideSystemUI();
                        } else {
                            showSystemUI();
                        }
                        return true;
                    }
                });
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return clickDetector.onTouchEvent(motionEvent);
            }
        });

        showSystemUI();
        nextBtnClick();
        prevBtnClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // When the window loses focus (e.g. the action overflow is shown),
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI.
        if (hasFocus) {
            delayedHide(INITIAL_HIDE_DELAY);
        } else {
            mHideHandler.removeMessages(0);
        }
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private final Handler mHideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
    }

    @Override
    protected void onDestroy() {
        mTpad.disconnectTPad();
        super.onDestroy();
    }

    // button1
    public void prevBtnClick() {
        Button prevBtn = (Button) findViewById(R.id.button1);
        prevBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        receiveCurrentImage--;
                        if(receiveCurrentImage < 0) {
                            receiveCurrentImage = receivedOrder.length-1;
                        }
                        else{
                            receiveCurrentImage = receiveCurrentImage % receivedOrder.length;
                        }
                        String path = ("sdcard/TPadImageRes/image"+receivedOrder[receiveCurrentImage]+".jpg");
                        Bitmap mImage = BitmapFactory.decodeFile(path);
                        mFrictionMapView.setDataBitmap(mImage);
                        mImage.recycle();
                    }
                }
        );
    }

    // button2
    public void ShowGrid(View view) {
        super.onBackPressed();
    }

    // button3
    public void nextBtnClick() {
        Button nextBtn = (Button) findViewById(R.id.button3);
        nextBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        receiveCurrentImage++;
                        receiveCurrentImage = receiveCurrentImage % receivedOrder.length;
                        String path = ("sdcard/TPadImageRes/image"+receivedOrder[receiveCurrentImage]+".jpg");
                        Bitmap mImage = BitmapFactory.decodeFile(path);
                        mFrictionMapView.setDataBitmap(mImage);
                        mImage.recycle();
                    }
                }
        );
    }

}
