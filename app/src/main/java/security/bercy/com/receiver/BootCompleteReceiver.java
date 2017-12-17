package security.bercy.com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.logging.Logger;

/**
 * Created by Bercy on 8/18/17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String sim = sp.getString("sim",null); //获取绑定的SIM

        if(!TextUtils.isEmpty(sim)) {
            //获取当前手机的sim
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = tm.getSimSerialNumber();

            if(sim.equals(currentSim)) {
                System.out.println("Its safety");
            }else {
                System.out.println("not safety");
            }
        }
    }
}


