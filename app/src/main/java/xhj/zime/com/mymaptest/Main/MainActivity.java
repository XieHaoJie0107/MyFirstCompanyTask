package xhj.zime.com.mymaptest.Main;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import xhj.zime.com.mymaptest.bean.TaskBean;

public class MainActivity extends BaseActivity {
    private NavigationView mNavigationView;
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
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString("userName", null);
        String userClassName = preferences.getString("userClassName", null);

        View view = mNavigationView.inflateHeaderView(R.layout.nav_header);
        TextView nameText = (TextView) view.findViewById(R.id.name_text);
        TextView classText = (TextView) view.findViewById(R.id.class_text);
        if (userName != null && userClassName != null) {
            nameText.setText(userName);
            classText.setText(userClassName);
        }

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

        mNavigationView.setCheckedItem(R.id.task_list);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.task_list:
                        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, null);
                        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setView(view1)
                                .create();
                        TextView task, flaw;
                        task = (TextView) view1.findViewById(R.id.task);
                        flaw = (TextView) view1.findViewById(R.id.flaw);
                        task.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.task_download:
                        String address = "http://192.168.1.225:8080/task/data/download?userid=26&pageSize=10&pageNo=1";
                        downloadTask(address);
                        break;
                    case R.id.task_upload:
                        break;
                    case R.id.finish_login:
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
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


    private void downloadTask(String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openProgressDialog();
            }
        });
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(MainActivity.this, "任务同步失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                TaskBean taskBean = Utility.handleTaskResponse(responseText);
                if (taskBean != null) {
                    SQLiteDatabase db = new SQLdm().openDatabase(MainActivity.this);
                    List<TaskBean.DataBean.TaskBeansBean> taskBeans = taskBean.getData().getTaskBeans();
                    ContentValues values = new ContentValues();
                    for (TaskBean.DataBean.TaskBeansBean taskBeansBean : taskBeans) {
                        String task_crew = taskBeansBean.getTask_crew();
                        String task_id = taskBeansBean.getTask_id();
                        String task_leader = taskBeansBean.getTask_leader();
                        String task_name = taskBeansBean.getTask_name();
                        String task_plan_tiam = taskBeansBean.getTask_plan_time().split(" ")[0];
                        String task_type = taskBeansBean.getTask_type();
                        String task_work_no = taskBeansBean.getTask_work_no();
                        String team_type = taskBeansBean.getTeam_type();
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String time = dateFormat.format(date);
                        String real = time.split(" ")[0];
                        int user_id = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getInt("userId", 26);
                        values.clear();
                        values.put("task_crew", task_crew);
                        values.put("task_id", task_id);
                        values.put("task_leader", task_leader);
                        values.put("task_name", task_name);
                        values.put("task_plan_time", task_plan_tiam);
                        values.put("task_type", task_type);
                        values.put("task_work_no", task_work_no);
                        values.put("team_type", team_type);
                        values.put("user_id", user_id);
                        values.put("task_confirm_time", real);
                        values.put("task_status", TASK_STATUS_WEIQIDONG);
                        db.insert("tasklist", null, values);
                    }
                    List<TaskBean.DataBean.TaskPointBeansBean> taskPointBeans = taskBean.getData().getTaskPointBeans();
                    for (TaskBean.DataBean.TaskPointBeansBean x : taskPointBeans) {
                        values.clear();
                        values.put("hasflaw", (String) x.getHasflaw());
                        values.put("object_type_id", x.getTask_id());
                        values.put("task_id", x.getTask_id());
                        values.put("task_point_id", x.getTask_point_id());
                        values.put("task_point_location", x.getTask_point_location());
                        values.put("task_point_name", x.getTask_point_name());
                        values.put("task_type", x.getTask_type());
                        db.insert("taskpoint", null, values);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
                    db.close();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "任务同步失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void openProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("任务同步中...");
            progressDialog.show();
        }
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
