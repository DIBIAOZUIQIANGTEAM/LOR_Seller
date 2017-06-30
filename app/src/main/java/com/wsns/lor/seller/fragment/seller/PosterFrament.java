package com.wsns.lor.seller.fragment.seller;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.activity.LoginActivity;
import com.wsns.lor.seller.activity.seller.UserInfoActivity;
import com.wsns.lor.seller.chatting.utils.FileHelper;
import com.wsns.lor.seller.entity.ServerDate;
import com.wsns.lor.seller.entity.User;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.ToastUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PosterFrament extends Fragment {
    final int REQUESTCODE_CAMERA = 1;
    final int REQUESTCODE_ALBUM = 2;
    final int REQUESTCODE_CUTTING = 3;

    ImageView img;
    Button button;


    SubscriberOnNextListener updatePostOnNext;
    Uri mUri;
    private String mPath;
    FrameLayout fl;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_seller_poster, null);
        img= (ImageView) view.findViewById(R.id.iv_background);
        button= (Button) view.findViewById(R.id.jmui_commit_btn);
        fl= (FrameLayout) view.findViewById(R.id.fl_post);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromLocal();
            }
        });
        updatePostOnNext=new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                ToastUtil.show(getActivity(),"申请成功");
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar(mUri.getPath());
            }
        });
        return view;
    }

    public void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent,REQUESTCODE_ALBUM);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
    
      if (requestCode == REQUESTCODE_ALBUM) {
            if (data != null) {
                Uri selectedImg = data.getData();
                if (selectedImg != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(selectedImg, filePathColumn, null, null, null);
                    try {
                        if (null == cursor) {
                            String path = selectedImg.getPath();
                            File file = new File(path);
                            if (file.isFile()) {
                                copyAndCrop(file);
                                return;
                            } else {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.picture_not_found),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (!cursor.moveToFirst()) {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);
                        if (path != null) {
                            File file = new File(path);
                            if (!file.isFile()) {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.picture_not_found),
                                        Toast.LENGTH_SHORT).show();
                                cursor.close();
                            } else {
                                //如果是选择本地图片进行头像设置，复制到临时文件，并进行裁剪
                                copyAndCrop(file);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if (requestCode == REQUESTCODE_CUTTING) {
          Picasso.with(getActivity()).load(mUri).fit().into(img);
        }
    }
    /**
     * 裁剪图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        getActivity().startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 复制后裁剪文件
     * @param file 要复制的文件
     */
    private void copyAndCrop(final File file) {
        FileHelper.getInstance().copyFile(file, getActivity(), new FileHelper.CopyFileCallback() {
            @Override
            public void copyCallback(Uri uri) {
                mUri = uri;
                cropRawPhoto(mUri);
            }
        });
    }
    /**
     * 上传广告
     */
    public void updateAvatar(String path) {
        if (path != null) {
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/*"), new File(path));
            HttpMethods.getInstance().updatePost(new ProgressSubscriber(updatePostOnNext, getActivity(), true), imgFile);
        }
    }
}
