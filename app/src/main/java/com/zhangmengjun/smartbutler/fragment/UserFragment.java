package com.zhangmengjun.smartbutler.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.MyUser;
import com.zhangmengjun.smartbutler.ui.LoginActivity;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.ShareUtils;
import com.zhangmengjun.smartbutler.utils.UtilTools;
import com.zhangmengjun.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhangmengjun.smartbutler.utils.UtilTools.putImageToShare;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.fragment
 * 文件名：UserFragment
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 16:30
 * 描述：个人中心
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private Button btn_exit_user;
    private TextView edit_user;

    private EditText user_name;
    private EditText user_gender;
    private EditText user_age;
    private EditText user_desc;

    //更新按钮
    private Button btn_update_ok;
    //圆形头像
    private CircleImageView profile_image;

    private CustomDialog customDialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        user_name = view.findViewById(R.id.user_name);
        user_gender = view.findViewById(R.id.user_gender);
        user_age = view.findViewById(R.id.user_age);
        user_desc = view.findViewById(R.id.user_desc);
        //个人信息默认是不可点击，不可输入的
        setEditable(false);

        btn_update_ok = view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //设置默认头像
        UtilTools.getImageFromShare(getActivity(),profile_image);



        //初始化dialog
        customDialog = new CustomDialog(getActivity(),-2,-2,R.layout.dialog_photo,R.style.pop_anim_style, Gravity.BOTTOM);
        //提示框外点击无效
        customDialog.setCancelable(false);

        btn_camera =customDialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = customDialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = customDialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);


        //设置具体的值
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        user_name.setText(myUser.getUsername());
        user_gender.setText(myUser.getSex() ? "男" : "女");
        user_age.setText(myUser.getAge() + "");
        user_desc.setText(myUser.getDesc());


        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }

    }

    private void setEditable(Boolean editable) {
        user_name.setEnabled(editable);
        user_age.setEnabled(editable);
        user_gender.setEnabled(editable);
        user_desc.setEnabled(editable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                //退出登录
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                setEditable(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //1.拿到输入框的值
                String username = user_name.getText().toString().trim();
                String userage = user_age.getText().toString().trim();
                String usergender = user_gender.getText().toString().trim();
                String userdesc = user_desc.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(userage) & !TextUtils.isEmpty(usergender)) {
                    //3。更新属性
                    MyUser myUser = new MyUser();
                    myUser.setUsername(username);
                    if (usergender.equals("男")) {
                        myUser.setSex(true);
                    } else {
                        myUser.setSex(false);
                    }
                    myUser.setAge(Integer.parseInt(userage));
                    if (!TextUtils.isEmpty(userdesc)) {
                        myUser.setDesc(userdesc);
                    } else {
                        myUser.setDesc("这个人很懒，神马都没有留下！！！");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    myUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                //修改成功
                                setEditable(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),"修改成功!!!",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getActivity(),"修改失败!!!",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), R.string.view_not_empty, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                customDialog.show();
                break;
            case R.id.btn_cancel:
                customDialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
        }

    }

    public static final String PHOTO_IMAGE_FILE_NAME="fileImg.jpg";
    public static final int  CAMERA_REQUEST_CODE =100;
    public static final int PICTURE_REQUEST_CODE =101;
    public static final int RESULT_REQUEST_CODE=102;
    private File tempFile = null;
    private Uri photoOutputUri = null; // 图片最终的输出文件的 Uri

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用即进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        customDialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICTURE_REQUEST_CODE);
        customDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != getActivity().RESULT_CANCELED){
            switch (requestCode){
                //相册返回的数据
                case PICTURE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机返回的数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    File file = new File(photoOutputUri.getPath());
                    if(file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());
                        profile_image.setImageBitmap(bitmap);
                        file.delete(); // 选取完后删除照片
                    } else {
                        Toast.makeText(getActivity(), "找不到照片", Toast.LENGTH_SHORT).show();
                    }
//                    if(data != null){
//                        L.e("返回数据非空！！！");
//                        //拿到图片设置
//                        //setImagetoView(data);
//                        //删除之前的图片
//                        if(tempFile != null){
//                            tempFile.delete();
//                        }
//                    }else{
//                        L.e("返回数据为空！！！");
//                    }
                    break;
            }
        }
    }
/*
因为 Android 7.0 的新特性规定，不同的应用之间不能再使用 file:// 类型的 Uri 共享数据了，
 否则会报异常，这就就是网上说的 Android 7.0 调用相机拍照崩溃的问题。官方推荐的做法是使用 FileProvider 来实现
    private void setImagetoView(Intent data) {
        Bundle bundle = data.getExtras();
        if(bundle != null){
            L.e("bundle返回数据非空！！！");
            Bitmap bitmap= bundle.getParcelable(MediaStore.EXTRA_OUTPUT);
            profile_image.setImageBitmap(bitmap);
            profile_image.setImageResource(R.drawable.black);
        }else{
            L.e("bundle返回数据为空！！！");
        }
    }
    */

    private void startPhotoZoom(Uri uri) {
        if(uri == null){
            L.e("uri is  null");
            return ;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //设置裁剪
        intent.putExtra("crop","true");
        //设置宽高比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //设置裁剪图片的质量
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);
        //发送数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoOutputUri=Uri.parse("file:////sdcard/image_output.jpg"));
//        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_REQUEST_CODE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存头像
        putImageToShare(getActivity(),profile_image);

    }
}
