package security.bercy.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import security.bercy.com.redstartsecurity.R;

/**
 * Created by Bercy on 8/11/17.
 * 第一个设置向导
 */

public class SetUp3Activity extends BaseSetupActivity {

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup3);

        etPhone = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public void showBackPage() {
        startActivity(new Intent(this, SetUp2Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, SetUp4Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    /*
    点击select contacts按钮触发事件
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        //回调onActivityResult方法
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Activity.RESULT_OK) {
            String phone = data.getStringExtra("phone");
            etPhone.setText(phone);

        }


    }
}
