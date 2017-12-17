package security.bercy.com.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import security.bercy.com.redstartsecurity.R;
import security.bercy.com.view.SettingItemView;

/**
 * setting
 * Created by Bercy on 8/3/17.
 */

public class SettingActivity extends Activity {
    SettingItemView sivUpdate;
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);

        //sivUpdate.setTitle("Auto Update");

        boolean autoUpdate = mPref.getBoolean("auto_update",true);
        if(autoUpdate) {
            //sivUpdate.setDesc("Enable");
            sivUpdate.setChecked(true);
        }else {
            //sivUpdate.setDesc("Disable");
            sivUpdate.setChecked(false);
        }


        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                    //sivUpdate.setDesc("Disable");
                    mPref.edit().putBoolean("auto_update",false).commit();
                }else {
                    sivUpdate.setChecked(true);
                   // sivUpdate.setDesc("Enable");
                    mPref.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
    }
}
