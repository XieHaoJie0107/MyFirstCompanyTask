package xhj.zime.com.mymaptest.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import xhj.zime.com.mymaptest.ActivityCollector.ActivityCollector;
import xhj.zime.com.mymaptest.ActivityCollector.BaseActivity;
import xhj.zime.com.mymaptest.Main.MainActivity;
import xhj.zime.com.mymaptest.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mAbout, mFinishAll;
    private ImageButton mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        mBack.setOnClickListener(this);
        mFinishAll.setOnClickListener(this);
        mAbout.setOnClickListener(this);
    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.setting_back_to_main);
        mFinishAll = (LinearLayout) findViewById(R.id.linearLayout5);
        mAbout = (LinearLayout) findViewById(R.id.linearLayout3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout5:
                ActivityCollector.finishAll();
                break;
            case R.id.setting_back_to_main:
                finish();
                break;
            case R.id.linearLayout3:
                openAboutUs();
                break;
        }
    }

    private void openAboutUs() {
        Intent intent = new Intent(this,SettingAboutUsActivity.class);
        startActivity(intent);
    }
}
