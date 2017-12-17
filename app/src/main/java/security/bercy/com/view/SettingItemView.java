package security.bercy.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import android.widget.RelativeLayout;
import android.widget.TextView;


import security.bercy.com.redstartsecurity.R;

/**
 * Created by Bercy on 8/4/17.
 */

public class SettingItemView extends RelativeLayout {

    TextView tvTitle;
    TextView tvDesc;
    CheckBox cbStatus;
    private static final String NAMESPACE = "http://schemas.android.com/apk/security.bercy.com.redstartsecurity";

    private String mTitle;
    private String mDescOn;
    private String mDescOff;


    public SettingItemView(Context context) {
        super(context);
        ininView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitle = attrs.getAttributeValue(NAMESPACE,"title");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        ininView();
        /*
        int attributeCount = attrs.getAttributeCount();
        for(int i = 0; i <attributeCount;i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            System.out.println(attributeName+"==="+attributeValue);
        }
        */
        //根据属性名称获取属性的值


    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ininView();

    }

    /*
        initail layout
     */
    private void ininView() {
        //自定义好的布局文件设置给当前的settingItemView
        View.inflate(getContext(), R.layout.view_setting_items, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

        setTitle(mTitle);//设置标题


    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    public boolean isChecked() {
        return cbStatus.isChecked();


    }

    public void setChecked(boolean check) {
        cbStatus.setChecked(check);
        //根据选择的状态 更新textview
        if(check) {
            setDesc(mDescOn);
        }else {
            setDesc(mDescOff);
        }
    }
}
