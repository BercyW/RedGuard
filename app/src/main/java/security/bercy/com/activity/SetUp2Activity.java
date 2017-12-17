package security.bercy.com.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import security.bercy.com.redstartsecurity.R;
import security.bercy.com.view.SettingItemView;

/**
 * Created by Bercy on 8/11/17.
 * 第一个设置向导
 */

public class SetUp2Activity extends BaseSetupActivity {

    SettingItemView sivSim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        sivSim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = config.getString("sim",null);
        if(!TextUtils.isEmpty(sim)) {
            sivSim.setChecked(true);
        }else  {
            sivSim.setChecked(false);
        }

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sivSim.isChecked()) {
                    sivSim.setChecked(false);
                    config.edit().remove("sim").commit();
                }else {
                    sivSim.setChecked(true);
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") String simSerialNumber = tm.getSimSerialNumber();
                    System.out.println(simSerialNumber);
                    config.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });
    }

    @Override
    public void showBackPage() {
        startActivity(new Intent(this, SetUp1Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, SetUp3Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

}
