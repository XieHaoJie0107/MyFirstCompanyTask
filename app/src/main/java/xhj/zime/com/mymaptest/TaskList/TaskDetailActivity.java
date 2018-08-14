package xhj.zime.com.mymaptest.TaskList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.SqliteDatabaseCollector.SQLdm;

public class TaskDetailActivity extends AppCompatActivity {
    TextView mtext1, mtext2, mtext3, mtext4, mtext5, mtext6, mtext7, mtext8, mtext9, mtext10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_task_detail);
        mtext1 = (TextView) findViewById(R.id.text1);
        mtext2 = (TextView) findViewById(R.id.text2);
        mtext3 = (TextView) findViewById(R.id.text3);
        mtext4 = (TextView) findViewById(R.id.text4);
        mtext5 = (TextView) findViewById(R.id.text5);
        mtext6 = (TextView) findViewById(R.id.text6);
        mtext7 = (TextView) findViewById(R.id.text7);
        mtext8 = (TextView) findViewById(R.id.text8);
        mtext9 = (TextView) findViewById(R.id.text9);
        mtext10 = (TextView) findViewById(R.id.text10);
        final FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        initData();
        ImageButton backToTaskList = (ImageButton) findViewById(R.id.setting_back_to_main);
        backToTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final Button flawJiLu = (Button) findViewById(R.id.flaw_jilu);
        final Button flawLuru = (Button) findViewById(R.id.flaw_luru);
        flawJiLu.setEnabled(false);
        flawLuru.setEnabled(true);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout, new TaskFlawObjectFragment());
        transaction.commit();
        flawJiLu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flawJiLu.setEnabled(false);
                flawLuru.setEnabled(true);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.layout, new TaskFlawObjectFragment());
                transaction.commit();
            }
        });
        flawLuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flawJiLu.setEnabled(true);
                flawLuru.setEnabled(false);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.layout, new TaskFlawObjectFragment2());
                transaction.commit();
            }
        });
    }

    private void initData() {
        SQLdm dm = new SQLdm();
        SQLiteDatabase database = dm.openDatabase(this);
        String taskId = "1";
        Cursor cursor = database.rawQuery("select * from objectattribute where task_id = ?", new String[]{taskId});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String text1 = cursor.getString(cursor.getColumnIndex("att_info0"));
                String text2 = cursor.getString(cursor.getColumnIndex("att_info1"));
                String text3 = cursor.getString(cursor.getColumnIndex("att_info10"));
                String text4 = cursor.getString(cursor.getColumnIndex("att_info11"));
                String text5 = cursor.getString(cursor.getColumnIndex("att_info2"));
                String text6 = cursor.getString(cursor.getColumnIndex("att_info3"));
                String text7 = cursor.getString(cursor.getColumnIndex("att_info4"));
                String text8 = cursor.getString(cursor.getColumnIndex("att_info5"));
                String text9 = cursor.getString(cursor.getColumnIndex("att_info6"));
                String text10 = cursor.getString(cursor.getColumnIndex("att_info7"));
                mtext1.setText(text1);
                mtext2.setText(text2);
                mtext3.setText(text3);
                mtext4.setText(text4);
                mtext5.setText(text5);
                mtext6.setText(text6);
                mtext7.setText(text7);
                mtext8.setText(text8);
                mtext9.setText(text9);
                mtext10.setText(text10);
            }
        }
    }
}
