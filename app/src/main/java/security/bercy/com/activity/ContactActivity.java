package security.bercy.com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import security.bercy.com.redstartsecurity.R;


/**
 * Created by Bercy on 9/3/17.
 */

public class ContactActivity extends Activity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ListView lvList;
    ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        lvList = (ListView) findViewById(R.id.lv_list);
        readContact = readContact();
        lvList.setAdapter(new SimpleAdapter(this, readContact, R.layout.contact_list_items, new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = readContact.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK,intent); //将数据放在intent里f返回给上一个页面
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContact();
                //点击允许过后如果没有以下代码，readcontact返回LIST之后不会填充listview
                readContact = readContact();
                lvList.setAdapter(new SimpleAdapter(this, readContact, R.layout.contact_list_items, new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<HashMap<String, String>> readContact() {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //requestPermissions 回调onrequestperemissionresult 方法
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
    /*
        1,从raw_Contact找到联系人的id（"contact_id"）
        2,根据contact_id从data表中查询出x相应的电话号码和联系人名称
        3.根据Mimetype区分哪个是联系人哪个是电话号码。
     */
            Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
            Uri dataUri = Uri.parse("content://com.android.contacts/data");
            //从raw_Contact找到联系人的id（"contact_id"）


            Cursor rawContactsCursor = getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
            if (rawContactsCursor != null) {
                while (rawContactsCursor.moveToNext()) {


                    String contactId = rawContactsCursor.getString(0);

                    /**
                     * 差的是view_data表
                     */
                    Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);

                    if (dataCursor != null) {

                        HashMap<String, String> map = new HashMap<>();

                        while (dataCursor.moveToNext()) {
                            String data1 = dataCursor.getString(0);
                            String mimetype = dataCursor.getString(1);
                            if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                                map.put("phone", data1);
                            } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                                map.put("name", data1);
                            }
                        }
                        list.add(map);
                        dataCursor.close();
                    }

                }
                rawContactsCursor.close();

            }

        }

        return list;
    }
}
