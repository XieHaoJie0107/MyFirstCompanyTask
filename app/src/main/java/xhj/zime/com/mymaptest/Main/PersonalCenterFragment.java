package xhj.zime.com.mymaptest.Main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.litepal.FluentQuery;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xhj.zime.com.mymaptest.ActivityCollector.ActivityCollector;
import xhj.zime.com.mymaptest.Login.LoginActivity;
import xhj.zime.com.mymaptest.Model.Express;
import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.SUser.TaskStatusString;
import xhj.zime.com.mymaptest.SqliteDatabaseCollector.SQLdm;
import xhj.zime.com.mymaptest.TaskList.TaskListActivity;
import xhj.zime.com.mymaptest.Util.HttpUtil;
import xhj.zime.com.mymaptest.Util.Utility;
import xhj.zime.com.mymaptest.bean.BaseDataBack;
import xhj.zime.com.mymaptest.bean.DataBean;
import xhj.zime.com.mymaptest.bean.TaskBeansBean;
import xhj.zime.com.mymaptest.bean.TaskPointBeansBean;

import static android.app.Activity.RESULT_OK;

public class PersonalCenterFragment extends Fragment implements View.OnClickListener {
    private TextView list, download, upload, powerOffLogin, userName, userClassName;
    private ProgressDialog progressDialog;
    private ImageView userImage;
    private static final String TAG = "-----------------";

    //定义意图返回的请求码,是拍摄照片还是去系统相册选择照片
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    //会用到数据共享器,拍摄的照片用uri作为定位共享出去
    private static Uri imageUri;

    private String imgPath;

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
        userImage = (ImageView) view.findViewById(R.id.user_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.setOnClickListener(this);
        download.setOnClickListener(this);
        upload.setOnClickListener(this);
        powerOffLogin.setOnClickListener(this);
        userImage.setOnClickListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName.setText(preferences.getString("userName", null));
        userClassName.setText(preferences.getString("userClassName", null));
        loadImg();
    }

    private void loadImg() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int userId = preferences.getInt("userId",-1);
        Log.i(TAG, "loadImg: "+userId);
        List<Express> expresses = LitePal.where("user_id = ?", String.valueOf(userId)).find(Express.class);
        if (expresses.size() > 0){
            Log.i(TAG, "loadImg: size > 0,设置数据");
            Express express = expresses.get(0);
            byte[] bytes = express.getUser_img();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Glide.with(this).load(bitmap).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userImage);
        }
//        SQLiteDatabase db = new SQLdm().openDatabase(getContext());
//        Cursor cursor = db.rawQuery("select * from express where user_id = ?",new String[]{userId+""});
//        if (cursor.moveToFirst()){
//            Log.i("---------------", "进来了: 初始化头像");
//            byte[] bytes = cursor.getBlob(cursor.getColumnIndex("user_img"));
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//            Glide.with(this).load(bitmap).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userImage);
//        }
//        db.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list:
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog, null);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
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
                String address = HttpUtil.baseUrl + "task/data/download?userid=26&pageSize=10&pageNo=1";
                downloadTask(address);
                break;
            case R.id.upload:
                break;
            case R.id.power_off_login:
                ActivityCollector.finishAll();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.user_image:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            default:
                break;
        }
    }

    private void saveImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                byte[] bytes = outputStream.toByteArray();
//                SQLiteDatabase db = new SQLdm().openDatabase(getContext());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                int userId = preferences.getInt("userId", -1);
                List<Express> expresses = LitePal.where("user_id = ?", String.valueOf(userId)).find(Express.class);
                if (expresses.size() > 0){
                    Log.i(TAG, "run: 更新头像信息");
                    Express express = new Express();
                    express.setUser_img(bytes);
                    express.updateAll("user_id = ?",String.valueOf(userId));
                }else {
                    Log.i(TAG, "run: 保存头像,设置头像成功");
                    Express express = new Express();
                    express.setUser_id(userId);
                    express.setUser_img(bytes);
                    express.save();
                }
//                Cursor cursor = db.rawQuery("select * from express where user_id = ?",new String[]{userId+""});
//                Log.i("---------------", String.valueOf(cursor.moveToNext()));
//                if (cursor.moveToFirst()){
//                    Log.i("---------------", userId + "更新头像");
//                    ContentValues values = new ContentValues();
//                    values.put("user_img", bytes);
//                    db.update("express", values, "user_id = ?",new String[]{userId+""});
//                }else {
//                    Log.i("------------", userId + "设置头像");
//                    ContentValues values = new ContentValues();
//                    values.put("user_id", userId);
//                    values.put("user_img", bytes);
//                    db.insert("express", null, values);
//                }
//                db.close();
            }
        }).start();
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    handleImageOnKitKat(data);
                }
                break;
            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.media.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        imgPath = imagePath;
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            saveImg();
            Glide.with(this).load(imagePath).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userImage);
        } else {
            Toast.makeText(getContext(), "加载图片失败!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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
                BaseDataBack baseDataBack = Utility.handleBaseDataBackResponse(responseText);
                Gson gson = new Gson();
                int backCode = baseDataBack.getCode();
                if (backCode > 0) {
                    SQLiteDatabase db = new SQLdm().openDatabase(getContext());
                    Object data = baseDataBack.getData();
                    DataBean dataBean = Utility.handleDataResponse(gson.toJson(data));
                    List<TaskBeansBean> taskBeans = dataBean.getTaskBeans();
                    ContentValues values = new ContentValues();
                    for (TaskBeansBean taskBeansBean : taskBeans) {
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
                        int user_id = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("userId", -1);
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
                        values.put("task_status", TaskStatusString.TASK_STATUS_WEIQIDONG);
                        db.insert("tasklist", null, values);
                    }
                    List<TaskPointBeansBean> taskPointBeans = dataBean.getTaskPointBeans();
                    for (TaskPointBeansBean x : taskPointBeans) {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
                    db.close();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "任务同步失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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





