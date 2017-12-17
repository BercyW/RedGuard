package security.bercy.com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import security.bercy.com.redstartsecurity.R;
import security.bercy.com.utils.StreamUtils;


public class SplashActivity extends Activity {

    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int ENTERHOME = 4;
    private TextView tvVersion;
    private TextView tvProgress; //download progress
    //m is server side
    private String mVersionName;
    private int mVersionCode;
    private String mDesc;
    private String mDownloadUrl;

    SharedPreferences mPref;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL ERROR", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "INTERNET ERROR", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "DATA ERROR", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case ENTERHOME:
                    enterHome();
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("Version: " + getVersionName());
        tvProgress = (TextView) findViewById(R.id.tv_progress);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            checkVersion();
        } else {
            /*
            延迟2秒再发消息
             */
            mHandler.sendEmptyMessageDelayed(ENTERHOME,2000);
        }

    }


    /*
        get version name;
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            System.out.println(versionName);
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /*
        get intlocal version code;
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /*
        get version from server and check
     */
    private void checkVersion() {

        final long startTime = System.currentTimeMillis();

        new Thread() {

            @Override
            public void run() {
                Message msg = Message.obtain();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        System.out.println("Server Return " + result);

                        //resolve json
                        JSONObject jo = new JSONObject(result);
                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadURL");
                        //System.out.println(mDesc);

                        if (mVersionCode > getVersionCode()) {
                            msg.what = CODE_UPDATE_DIALOG;

                        } else {
                            msg.what = ENTERHOME;
                        }
                    }

                } catch (MalformedURLException e) {
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = CODE_JSON_ERROR;
                   e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;
                    //强制休眠保证闪屏展示2秒
                    if (timeUsed < 2000) {
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

        }.start();

    }

    //update dialog
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Version:" + mVersionName);
        builder.setMessage(mDesc);
        //builder.setCancelable(false);// can not go back

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }


        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        //set up cancel listener, user hit back button
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    //download update
    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            tvProgress.setVisibility(View.VISIBLE);// show progress

            String target = Environment.getExternalStorageDirectory() + "/update.apk";
            //XUtils
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

                @Override
                //download progressing
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("current downloading:" + current + "/" + total);
                    tvProgress.setText("Loading:" + current * 100 / total + "%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    System.out.println("Download success");
                    //to system 安装 page
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    /*
                    跳转到系统安装页面，activity: .PackageInstallerActivity
                        filter 里边有mimeType 在packageInstaller里的manifest.xml文件里找
                     */
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    //startActivity(intent);
                    startActivityForResult(intent, 0);//if user cancel the install, will call back onActivityResult methord ;
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "Download Fail", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SplashActivity.this, "No SD Card Found", Toast.LENGTH_SHORT).show();
        }
    }

    //cancel the download will enterhome
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);

    }

    //get in index
    public void enterHome() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

}
