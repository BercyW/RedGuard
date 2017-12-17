package security.bercy.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import security.bercy.com.redstartsecurity.R;

/**
 * Created by Bercy on 8/11/17.
 * 第一个设置向导
 */

public class SetUp1Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showBackPage() {

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,SetUp2Activity.class));
        finish();
        //切换动画
        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
    }


}
