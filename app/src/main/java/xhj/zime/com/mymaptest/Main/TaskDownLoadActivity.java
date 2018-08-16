package xhj.zime.com.mymaptest.Main;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.TaskList.BaseSpinnerAdapter;
import xhj.zime.com.mymaptest.TaskList.SpinnerChooseAdapter;
import xhj.zime.com.mymaptest.TaskList.SpinnerUtils;

public class TaskDownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBack;
    private ImageButton mTimeStart, mTimeEnd;
    private Button mDownload;
    private TextView mTimeStartToEnd;
    private SpinnerChooseAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private SpinnerUtils mSpinnerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_down_load);
        initView();
        mBack.setOnClickListener(this);
        mTimeStart.setOnClickListener(this);
        mTimeEnd.setOnClickListener(this);
        mDownload.setOnClickListener(this);

        initData();
        mAdapter = new SpinnerChooseAdapter(this, mList, new BaseSpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mTimeStartToEnd.setText(mList.get(position));
                if (mSpinnerUtils != null) {
                    mSpinnerUtils.closeSpinner();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mTimeStartToEnd.setText(mList.get(position));
            }
        });
        mSpinnerUtils= new SpinnerUtils(this,mTimeStartToEnd,mAdapter);
        mSpinnerUtils.init();
    }

    private void initData() {
        mList.add("本周");
        mList.add("本月");
        mList.add("自定义");
    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.setting_back_to_main);
        mTimeEnd = (ImageButton) findViewById(R.id.btn_time_end);
        mTimeStart = (ImageButton) findViewById(R.id.btn_time_start);
        mDownload = (Button) findViewById(R.id.btn_download);
        mTimeStartToEnd = findViewById(R.id.tv_time_start_to_end);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back_to_main:
                finish();
                break;
            case R.id.btn_time_start:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this,null,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_time_end:
                Calendar calendar2 = Calendar.getInstance();
                DatePickerDialog dialog2 = new DatePickerDialog(this,null,calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),calendar2.get(Calendar.DAY_OF_MONTH));
                dialog2.show();
                break;
            case R.id.btn_download:
                View view1 = LayoutInflater.from(this).inflate(R.layout.alert_dialog_download,null);
                new AlertDialog.Builder(this)
                        .setView(view1)
                        .setCancelable(true)
                        .create().show();
                break;
        }
    }
}
