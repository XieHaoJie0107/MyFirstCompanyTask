package xhj.zime.com.mymaptest.Main;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xhj.zime.com.mymaptest.ActivityCollector.ActivityCollector;
import xhj.zime.com.mymaptest.ActivityCollector.BaseActivity;
import xhj.zime.com.mymaptest.Login.LoginActivity;
import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.Settings.SettingActivity;
import xhj.zime.com.mymaptest.SqliteDatabaseCollector.SQLdm;
import xhj.zime.com.mymaptest.TaskList.TaskListActivity;
import xhj.zime.com.mymaptest.Util.HttpUtil;
import xhj.zime.com.mymaptest.Util.Utility;
import xhj.zime.com.mymaptest.bean.BaseDataBean;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "------------------------";
    private ProgressDialog progressDialog;
    public static final int TASK_STATUS_WANGCHENG = 0;
    public static final int TASK_STATUS_DANGQIAN = 1;
    public static final int TASK_STATUS_YIQIDONG = 2;
    public static final int TASK_STATUS_WEIQIDONG = 3;
    public static final int TASK_STATUS_SHANGCHUAN_SHIBAI = 4;
    public static final int TASK_STATUS_SHANGCHUAN_CHENGGONG = 5;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        syncBaseData();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String userName = preferences.getString("userName", null);
//        String userClassName = preferences.getString("userClassName", null);
//
//        View view = mNavigationView.inflateHeaderView(R.layout.nav_header);
//        TextView nameText = (TextView) view.findViewById(R.id.name_text);
//        TextView classText = (TextView) view.findViewById(R.id.class_text);
//        if (userName != null && userClassName != null) {
//            nameText.setText(userName);
//            classText.setText(userClassName);
//        }

        ImageButton setting_img_btn = (ImageButton) findViewById(R.id.setting_img_btn);
        setting_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        ImageButton personal_img_btn = (ImageButton) findViewById(R.id.personal_img_btn);
        personal_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void syncBaseData() {
        String address = "http://192.168.1.225:8080/data/base?version=0";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "基础数据同步失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                BaseDataBean baseDataBean = Utility.handleBaseDataResponse(responseText);
                String status = baseDataBean.getMsg();
                if ("请求成功".equals(status)) {
                    SQLiteDatabase db = new SQLdm().openDatabase(MainActivity.this);
                    ContentValues values = new ContentValues();
                    List<BaseDataBean.DataBean.FlawDisposesBean> flawDisposes = baseDataBean.getData().getFlawDisposes();
                    for (BaseDataBean.DataBean.FlawDisposesBean x : flawDisposes) {
                        values.clear();
                        values.put("flaw_dispose_type_id", x.getFlaw_dispose_type_id());
                        values.put("flaw_dispose_type_name", x.getFlaw_dispose_type_name());
                        db.insert("flawdispose", null, values);
                    }

                    List<BaseDataBean.DataBean.FlawLevelsBean> flawLevels = baseDataBean.getData().getFlawLevels();
                    for (BaseDataBean.DataBean.FlawLevelsBean x : flawLevels) {
                        values.clear();
                        values.put("flaw_level_id", x.getFlaw_level_id());
                        values.put("flaw_level_name", x.getFlaw_level_name());
                        db.insert("flawlevel", null, values);
                    }

                    List<BaseDataBean.DataBean.FlawTypesBean> flawTypes = baseDataBean.getData().getFlawTypes();
                    for (BaseDataBean.DataBean.FlawTypesBean x : flawTypes) {
                        values.clear();
                        values.put("flaw_type_id", x.getFlaw_type_id());
                        values.put("flaw_type_name", x.getFlaw_type_name());
                        values.put("flaw_type_template", x.getFlaw_type_template());
                        values.put("obj_type_id", x.getObj_type_id());
                        db.insert("flawtype", null, values);
                    }

                    List<BaseDataBean.DataBean.ObjectTypesBean> objectTypes = baseDataBean.getData().getObjectTypes();
                    for (BaseDataBean.DataBean.ObjectTypesBean x : objectTypes) {
                        values.clear();
                        values.put("object_id", x.getObject_id());
                        values.put("object_name", x.getObject_name());
                        db.insert("objecttype", null, values);
                    }

                    List<BaseDataBean.DataBean.WorkTypesBean> workTypes = baseDataBean.getData().getWorkTypes();
                    for (BaseDataBean.DataBean.WorkTypesBean x : workTypes) {
                        values.clear();
                        values.put("type_id", x.getType_id());
                        values.put("type_name", x.getType_name());
                        db.insert("worktype", null, values);
                    }
                    db.close();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "基础数据同步失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
