package xhj.zime.com.mymaptest.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xhj.zime.com.mymaptest.ActivityCollector.BaseActivity;
import xhj.zime.com.mymaptest.Main.MainActivity;
import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.SUser.MyUserString;
import xhj.zime.com.mymaptest.Util.HttpUtil;
import xhj.zime.com.mymaptest.Util.Utility;
import xhj.zime.com.mymaptest.bean.BaseDataBack;
import xhj.zime.com.mymaptest.bean.UserBean;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    SharedPreferences preferences;
    EditText user_edit, password_edit;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        String user = preferences.getString(MyUserString.userString, "liyu");
        String password = preferences.getString(MyUserString.passwordString, "123456");
        user_edit.setText(user);
        password_edit.setText(password);
        loginBtn.setOnClickListener(this);
    }

    private void initView() {
        user_edit = (EditText) findViewById(R.id.user_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    private void queryFromService() {
        String loginName = user_edit.getText().toString();
        String password = password_edit.getText().toString();
        String address = HttpUtil.baseUrl + "user/login?loginName=" + loginName + "&password=" + password;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络请求失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                UserBean userBean = Utility.handleUserResponse(responseText);
                userBean.getCode();
                if ("请求成功".equals(userBean.getMsg())) {
                    String userClassName = userBean.getData().getOrg() + "/" + userBean.getData().getGroup() + "/" + userBean.getData().getGroupName();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", userBean.getData().getLoginName());
                    editor.putString("password", userBean.getData().getPassword());
                    editor.putInt("userId", userBean.getData().getUserId());
                    editor.putString("userName", userBean.getData().getUserName());
                    editor.putString("userClassName", userClassName);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                            user_edit.setText("");
                            password_edit.setText("");
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                queryFromService();
                break;
        }
    }
}

