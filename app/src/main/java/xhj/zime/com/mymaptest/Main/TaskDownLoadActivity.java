package xhj.zime.com.mymaptest.Main;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.TaskList.BaseSpinnerAdapter;
import xhj.zime.com.mymaptest.TaskList.SpinnerChooseAdapter;
import xhj.zime.com.mymaptest.TaskList.SpinnerUtils;

public class TaskDownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBack;
    private ImageButton mTimeStart, mTimeEnd;
    private Button mDownload;
    private TextView mMarqueeText;
    private TextView mTextStart, mTextEnd;
    private TextView mTimeStartToEnd;
    private SpinnerChooseAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private SpinnerUtils mSpinnerUtils;
    private static final String TAG = "----------------------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_down_load);
        initView();
        mBack.setOnClickListener(this);
        mTimeStart.setOnClickListener(this);
        mTimeEnd.setOnClickListener(this);
        mDownload.setOnClickListener(this);
        mMarqueeText.setSelected(true);

        initData();
        mAdapter = new SpinnerChooseAdapter(this, mList, new BaseSpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mTimeStartToEnd.setText(mList.get(position));
                if (mSpinnerUtils != null) {
                    mSpinnerUtils.closeSpinner();
                }

                if ("本周".equals(mTimeStartToEnd.getText())) {
                    long l = System.currentTimeMillis();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    long beginTime = l - (dayOfWeek - 1) * (24 * 60 * 60 * 1000);
                    long endTime = l + (7 - dayOfWeek) * (24 * 60 * 60 * 1000);
                    String dateBegin = format.format(beginTime);
                    String dateEnd = format.format(endTime);
                    mTextStart.setText(dateBegin);
                    mTextEnd.setText(dateEnd);
                } else if ("本月".equals(mTimeStartToEnd.getText())) {
                    long l = System.currentTimeMillis();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int actualMaximum = calendar.getActualMaximum(Calendar.DATE);
                    long beginTime = l - (dayOfMonth - 1) * (24 * 60 * 60 * 1000);
                    long endTime = l + (actualMaximum - dayOfMonth) * (24 * 60 * 60 * 1000);
                    String dateBegin = format.format(beginTime);
                    String dateEnd = format.format(endTime);
                    mTextStart.setText(dateBegin);
                    mTextEnd.setText(dateEnd);
                } else if ("自定义".equals(mTimeStartToEnd.getText())) {
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mTimeStartToEnd.setText(mList.get(position));
            }
        });
        mSpinnerUtils = new SpinnerUtils(this, mTimeStartToEnd, mAdapter);
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
        mTextStart = (TextView) findViewById(R.id.tv_start);
        mTextEnd = (TextView) findViewById(R.id.tv_end);
        mMarqueeText = (TextView) findViewById(R.id.tv_marquee);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back_to_main:
                finish();
                break;
            case R.id.btn_time_start:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, null, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(i + "-" + (i1 + 1) + "-" + i2);
                        mTextStart.setText(sb.toString());
                    }
                });
                dialog.show();
                break;
            case R.id.btn_time_end:
                Calendar calendar2 = Calendar.getInstance();
                DatePickerDialog dialog2 = new DatePickerDialog(this, null, calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                dialog2.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(i + "-" + (i1 + 1) + "-" + i2);
                        String start = mTextStart.getText().toString();
                        String end = sb.toString();
                        mTextEnd.setText(end);
                        boolean isAllowShowEndTime = compareToBegin(start, end);
                        Log.i(TAG, start + "   " + end);
                    }
                });
                dialog2.show();
                break;
            case R.id.btn_download:
                View view1 = LayoutInflater.from(this).inflate(R.layout.alert_dialog_download, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setView(view1)
                        .setCancelable(true)
                        .create();
                alertDialog.show();
                Button mBtnSure = view1.findViewById(R.id.btn_sure);
                mBtnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                break;
        }
    }

    private boolean compareToBegin(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);
            int status = startDate.compareTo(endDate);
            if (status > 0) {
                Toast.makeText(this,"结束日期不能在开始日期之前",Toast.LENGTH_SHORT).show();
                mTextEnd.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
