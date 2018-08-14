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

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        ImageButton seetting_img_back_to_main = (ImageButton) findViewById(R.id.setting_back_to_main);
        seetting_img_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.finishAll();
            }
        });
    }
}
