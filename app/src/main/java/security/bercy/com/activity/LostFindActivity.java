package security.bercy.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import security.bercy.com.redstartsecurity.R;

/**
 * Created by Bercy on 8/11/17.
 */

public class LostFindActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = mPrefs.getBoolean("configed", false); //判断是否进入过设置向导 默认false
        if (configed) {
            setContentView(R.layout.activity_lost_find);
        } else {
            //跳转设置向导页面
            startActivity(new Intent(this, SetUp1Activity.class));
            finish();

        }
 
    }


    public void reEnter(View view) {
        startActivity(new Intent(this, SetUp1Activity.class));
        finish();
    }
}
