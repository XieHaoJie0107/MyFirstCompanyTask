package xhj.zime.com.mymaptest.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xhj.zime.com.mymaptest.ActivityCollector.ActivityCollector;
import xhj.zime.com.mymaptest.Login.LoginActivity;
import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.TaskList.TaskListActivity;
import xhj.zime.com.mymaptest.Util.HttpUtil;

public class PersonalCenterFragment extends Fragment implements View.OnClickListener {
    private TextView list, download, upload, powerOffLogin,userName,userClassName;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_header, container, false);
        list = (TextView) view.findViewById(R.id.list);
        download = (TextView) view.findViewById(R.id.download);
        upload = (TextView) view.findViewById(R.id.upload);
        powerOffLogin = (TextView) view.findViewById(R.id.power_off_login);
        userName = (TextView) view.findViewById(R.id.name_text);
        userClassName = (TextView) view.findViewById(R.id.class_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.setOnClickListener(this);
        download.setOnClickListener(this);
        upload.setOnClickListener(this);
        powerOffLogin.setOnClickListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName.setText(preferences.getString("userName",null));
        userClassName.setText(preferences.getString("userClassName",null));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list:
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(view1)
                        .create();
                TextView task, flaw;
                task = (TextView) view1.findViewById(R.id.task);
                flaw = (TextView) view1.findViewById(R.id.flaw);
                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), TaskListActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;
            case R.id.download:
                String address = "http://192.168.1.225:8080/task/data/download?userid=26&pageSize=10&pageNo=1";
                downloadTask(address);
                break;
            case R.id.upload:
                break;
            case R.id.power_off_login:
                ActivityCollector.finishAll();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void downloadTask(String address) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openProgressDialog();
            }
        });
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "任务同步失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
//                TaskBean taskBean = Utility.handleTaskResponse(responseText);
//                if (taskBean != null) {
//                    SQLiteDatabase db = new SQLdm().openDatabase(MainActivity.this);
//                    List<TaskBean.DataBean.TaskBeansBean> taskBeans = taskBean.getData().getTaskBeans();
//                    ContentValues values = new ContentValues();
//                    for (TaskBean.DataBean.TaskBeansBean taskBeansBean : taskBeans) {
//                        String task_crew = taskBeansBean.getTask_crew();
//                        String task_id = taskBeansBean.getTask_id();
//                        String task_leader = taskBeansBean.getTask_leader();
//                        String task_name = taskBeansBean.getTask_name();
//                        String task_plan_tiam = taskBeansBean.getTask_plan_time().split(" ")[0];
//                        String task_type = taskBeansBean.getTask_type();
//                        String task_work_no = taskBeansBean.getTask_work_no();
//                        String team_type = taskBeansBean.getTeam_type();
//                        Date date = new Date();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        String time = dateFormat.format(date);
//                        String real = time.split(" ")[0];
//                        int user_id = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getInt("userId", 26);
//                        values.clear();
//                        values.put("task_crew", task_crew);
//                        values.put("task_id", task_id);
//                        values.put("task_leader", task_leader);
//                        values.put("task_name", task_name);
//                        values.put("task_plan_time", task_plan_tiam);
//                        values.put("task_type", task_type);
//                        values.put("task_work_no", task_work_no);
//                        values.put("team_type", team_type);
//                        values.put("user_id", user_id);
//                        values.put("task_confirm_time", real);
//                        values.put("task_status", TASK_STATUS_WEIQIDONG);
//                        db.insert("tasklist", null, values);
//                    }
//                    List<TaskBean.DataBean.TaskPointBeansBean> taskPointBeans = taskBean.getData().getTaskPointBeans();
//                    for (TaskBean.DataBean.TaskPointBeansBean x : taskPointBeans) {
//                        values.clear();
//                        values.put("hasflaw", (String) x.getHasflaw());
//                        values.put("object_type_id", x.getTask_id());
//                        values.put("task_id", x.getTask_id());
//                        values.put("task_point_id", x.getTask_point_id());
//                        values.put("task_point_location", x.getTask_point_location());
//                        values.put("task_point_name", x.getTask_point_name());
//                        values.put("task_type", x.getTask_type());
//                        db.insert("taskpoint", null, values);
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            closeProgressDialog();
//                        }
//                    });
//                    db.close();
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(MainActivity.this, "任务同步失败!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        });
    }

    private void openProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
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
