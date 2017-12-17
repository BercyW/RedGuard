package security.bercy.com.activity;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import security.bercy.com.redstartsecurity.R;
import security.bercy.com.utils.MD5Utils;

/**
 * Created by Bercy on 7/25/17.
 */

public class IndexActivity extends Activity {
    private GridView gvHome;
    private String[] mItems = new String[]{"Lost", "SoftWare", "Contact", "Task", "Data", "Virus",
            "Cache", "Tools", "Setting"};
    private int[] mPics = new int[]{R.drawable.home_safe, R.drawable.home_apps, R.drawable.home_callmsgsafe,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
            R.drawable.home_sysoptimize, R.drawable.home_tools,
            R.drawable.home_settings};
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());
        /*
            set up listener
         */
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 手机防盗
                        showPasswordDialog();
                        break;
                    case 8: //setting
                        startActivity(new Intent(IndexActivity.this, SettingActivity.class));

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示密码弹窗
     */
    protected void showPasswordDialog() {
        // 判断是否设置密码
        String savedPassword = mPref.getString("password", null);
        if (!TextUtils.isEmpty(savedPassword)) {
            // 输入密码弹窗
            showPasswordInputDialog();
        } else {
            // 如果没有设置过, 弹出设置密码的弹窗
            showPasswordSetDailog();
        }
    }

    /**
     * 输入密码弹窗
     */
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_input_password, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final EditText etPassword = (EditText) view
                .findViewById(R.id.et_password);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(password)) {
                    String savedPassword = mPref.getString("password", null);

                    if (MD5Utils.encode(password).equals(savedPassword)) {
                        // Toast.makeText(HomeActivity.this, "登录成功!",
                        // Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // 跳转到手机防盗页
                        startActivity(new Intent(IndexActivity.this,
                                LostFindActivity.class));
                    } else {
                        Toast.makeText(IndexActivity.this, "密码错误!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IndexActivity.this, "输入框内容不能为空!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });

        dialog.show();
    }

    /**
     * 设置密码的弹窗
     */
    private void showPasswordSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_set_password, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final EditText etPassword = (EditText) view
                .findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view
                .findViewById(R.id.et_password_confirm);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                // password!=null && !password.equals("")
                if (!TextUtils.isEmpty(password) && !passwordConfirm.isEmpty()) {
                    if (password.equals(passwordConfirm)) {
                        // Toast.makeText(HomeActivity.this, "登录成功!",
                        // Toast.LENGTH_SHORT).show();

                        // 将密码保存起来
                        mPref.edit()
                                .putString("password",
                                        MD5Utils.encode(password)).commit();

                        dialog.dismiss();

                        // 跳转到手机防盗页
                        startActivity(new Intent(IndexActivity.this,
                                LostFindActivity.class));
                    } else {
                        Toast.makeText(IndexActivity.this, "两次密码不一致!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IndexActivity.this, "输入框内容不能为空!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });

        dialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(IndexActivity.this, R.layout.home_list_items, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
            ivItem.setImageResource(mPics[position]);
            tvItem.setText(mItems[position]);
            return view;
        }
    }
}
