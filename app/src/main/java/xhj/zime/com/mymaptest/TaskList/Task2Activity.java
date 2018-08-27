package xhj.zime.com.mymaptest.TaskList;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xhj.zime.com.mymaptest.Main.MainActivity;
import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.SqliteDatabaseCollector.SQLdm;

public class Task2Activity extends AppCompatActivity {
    private ImageButton back;
    private ListView listView;
    private TextView taskName;
    private Button qidong, zanting, wancheng;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        initView();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Task2Activity.this,TaskDetailActivity.class);
                startActivity(intent);
            }
        });
        initData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        qidong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = new SQLdm().openDatabase(Task2Activity.this);
                ContentValues values = new ContentValues();
                values.put("task_status", MainActivity.TASK_STATUS_YIQIDONG);
                db.update("tasklist",values,"task_name = ?",new String[]{"建华大街隧道巡检"});
                db.close();
                qidong.setEnabled(false);
                zanting.setEnabled(true);
                wancheng.setEnabled(true);
            }
        });
        zanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = new SQLdm().openDatabase(Task2Activity.this);
                ContentValues values = new ContentValues();
                values.put("task_status", MainActivity.TASK_STATUS_WEIQIDONG);
                db.update("tasklist",values,"task_name = ?",new String[]{"建华大街隧道巡检"});
                db.close();
                qidong.setEnabled(true);
                zanting.setEnabled(false);
                wancheng.setEnabled(true);
            }
        });
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = new SQLdm().openDatabase(Task2Activity.this);
                ContentValues values = new ContentValues();
                values.put("task_status", MainActivity.TASK_STATUS_WANGCHENG);
                db.update("tasklist",values,"task_name = ?",new String[]{"建华大街隧道巡检"});
                db.close();
                qidong.setEnabled(false);
                zanting.setEnabled(false);
                wancheng.setEnabled(false);
            }
        });
    }

    private void initData() {
        SQLiteDatabase db = new SQLdm().openDatabase(this);
        Cursor cursor = db.rawQuery("select * from taskpoint where task_type = ?",new String[]{"401"});
        while (cursor.moveToNext()){
             String task_point_name = cursor.getString(cursor.getColumnIndex("task_point_name"));
             list.add(task_point_name);
        }
        String taskName = getIntent().getStringExtra("taskName");
        this.taskName.setText(taskName);
        adapter.notifyDataSetChanged();
        db.close();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        taskName = (TextView) findViewById(R.id.task_name);
        qidong = (Button) findViewById(R.id.qidong);
        zanting = (Button) findViewById(R.id.zanting);
        wancheng = (Button) findViewById(R.id.wancheng);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        back = (ImageButton) findViewById(R.id.back);
    }
}
