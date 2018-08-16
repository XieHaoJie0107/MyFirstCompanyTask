package xhj.zime.com.mymaptest.Main;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.util.Calendar;

import xhj.zime.com.mymaptest.R;

public class TaskDownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBack;
    private ImageButton mTimeStart, mTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_down_load);
        initView();
        mBack.setOnClickListener(this);
        mTimeStart.setOnClickListener(this);
        mTimeEnd.setOnClickListener(this);
    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.setting_back_to_main);
        mTimeEnd = (ImageButton) findViewById(R.id.btn_time_end);
        mTimeStart = (ImageButton) findViewById(R.id.btn_time_start);
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
        }
    }
}
