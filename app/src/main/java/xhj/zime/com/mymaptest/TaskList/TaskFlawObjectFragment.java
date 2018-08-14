package xhj.zime.com.mymaptest.TaskList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xhj.zime.com.mymaptest.Main.MainActivity;
import xhj.zime.com.mymaptest.R;

import static android.app.Activity.RESULT_OK;

public class TaskFlawObjectFragment extends Fragment {
    ImageView photo1, photo2;
    Button add_photo;
    private static final int TAKE_PHOTO = 1;//拍照操作

    //拍照所得到的图像的保存路径
    private Uri imageUri;

    //当前用户拍照或者从相册选择的照片的文件名
    private String fileName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flaw_object, container, false);
        photo1 = (ImageView) view.findViewById(R.id.photo1);
        photo2 = (ImageView) view.findViewById(R.id.photo2);
        add_photo = (Button) view.findViewById(R.id.add_photo);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePohto();
            }
        });
    }

    public void takePohto() {
        //  用时间戳的方式来命名图片文件，这样可以避免文件名称重复
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        fileName = "easyasset" + format.format(date);
        // 创建一个File对象，用于存放拍照所得到的照片
        File outputImage = new File(getContext().getExternalCacheDir(), fileName + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将File对象转换为Uri对象，以便拍照后保存
        imageUri = FileProvider.getUriForFile(getActivity(),
                "xhj.zime.com.mymaptest.fileprovider", outputImage);
        //启动系统的照相Intent
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //Android系统自带的照相intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        startActivityForResult(intent, TAKE_PHOTO); //以forResult模式启动照相intent
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().
                                getContentResolver().openInputStream(imageUri));
                        photo1.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

}



