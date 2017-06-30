package com.wsns.lor.seller.fragment.regist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.User;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.MD5;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/1/5.
 */

public class RegisterThirdFragment extends Fragment{

    View view;
    Activity activity;

    String phone = "";
    String password;
    Button btnNext;
    ImageView ivback;
    EditText etPassword;
    EditText etRepatedPassword;
    private SubscriberOnNextListener getRegisterResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_regist_third,null);
            activity = getActivity();
            SharedPreferences sharedPreferences = activity.getSharedPreferences("regist", Context.MODE_PRIVATE);
            phone = sharedPreferences.getString("phone","");
            init();
        }
        return view;
    }

    void init(){
        btnNext = (Button) view.findViewById(R.id.btn_next);
        ivback = (ImageView) view.findViewById(R.id.iv_back);
        etPassword = (EditText) view.findViewById(R.id.et_mobile);
        etRepatedPassword = (EditText) view.findViewById(R.id.et_repated_password);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });


        getRegisterResult = new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                registSuccess(user);
            }
        };
    }

    private void registSuccess(User user) {

        /**=================     调用SDK注册接口    =================*/
        JMessageClient.register(phone, MD5.getMD5(password), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                if (responseCode == 0) {
                    goNext();
                    Log.i("RegisterActivity", "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);

                } else {
                    Toast.makeText(activity, "注册失败", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });

    }

    public void regist(){
          password = etPassword.getText().toString();
        if(!password.equals(etRepatedPassword.getText().toString())){
            Toast.makeText(activity,"密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        HttpMethods.getInstance().getRegisterResult(new ProgressSubscriber(getRegisterResult, activity,true),phone , MD5.getMD5(password));


    }


    public static interface OnGoNextListener{
        void onGoNext();
    }

    OnGoNextListener onGoNextListener;

    public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
        this.onGoNextListener = onGoNextListener;
    }

    void goNext(){
        if(onGoNextListener!=null){
            onGoNextListener.onGoNext();
        }
    }

    public static interface OnGoBackListener{
        void onGoBack();
    }
    OnGoBackListener onGoBackListener;
    public void setOnGoBackListener(OnGoBackListener onGoBackListener){
        this.onGoBackListener = onGoBackListener;
    }
    public void goBack(){
        if(onGoBackListener!=null){
            onGoBackListener.onGoBack();
        }
    }

}
