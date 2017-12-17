package security.bercy.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import security.bercy.com.redstartsecurity.R;


/**
 * Created by Bercy on 8/17/17.
 */

public abstract class BaseSetupActivity extends Activity {
    GestureDetector mDectector;
    public SharedPreferences config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = getSharedPreferences("config", MODE_PRIVATE);
        mDectector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //判断纵向滑动幅度是否过大
                if(Math.abs(e2.getRawY()-e1.getRawY())>100) {
                    return true;
                }

                if (e2.getRawX() - e1.getRawX() > 100) {
                    showBackPage();

                }
                if (e1.getRawX() - e2.getRawX() > 100) {
                    showNextPage();
                }

                if(Math.abs(velocityX)<150) {
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 子类必须实现
     */
    public abstract void showBackPage();

    public abstract void showNextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDectector.onTouchEvent(event);//委托收拾识别器处理触摸事件
        return super.onTouchEvent(event);
    }
    public void next(View view) {
        showNextPage();


    }

    public void back(View view) {
        showBackPage();
    }
}

