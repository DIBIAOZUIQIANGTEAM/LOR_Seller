package com.wsns.lor.seller.fragment.seller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.activity.order.OrdersCommenActivity;
import com.wsns.lor.seller.activity.seller.CheckConsumptionRecordsActivity;
import com.wsns.lor.seller.activity.seller.SellerGoodsEditActivity;
import com.wsns.lor.seller.activity.seller.SellerInfoEditActivity;
import com.wsns.lor.seller.activity.seller.SellerPostctivity;
import com.wsns.lor.seller.activity.seller.UserInfoActivity;
import com.wsns.lor.seller.application.OnlineUserInfo;
import com.wsns.lor.seller.entity.Seller;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wsns.lor.seller.application.OnlineUserInfo.seller;

;


public class MyProfileFragment extends Fragment {
    View view;
    Activity activity;
    ImageView av;
    TextView tvName;
    TextView tvMoney;

    RelativeLayout relativeLayout, rlMe;
    SubscriberOnNextListener getCurrentUser;
    @BindView(R.id.linearLayout_goods_edit)
    LinearLayout linearLayoutGoodsEdit;
    @BindView(R.id.linearLayout_info_edit)
    LinearLayout linearLayoutInfoEdit;
    @BindView(R.id.linearLayout_picture_edit)
    LinearLayout linearLayoutPictureEdit;
    @BindView(R.id.linearLayout_post_edit)
    LinearLayout linearLayoutPostEdit;
    @BindView(R.id.linearLayout0)
    RelativeLayout linearLayout0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_my_profile, null);
            activity = getActivity();
            av = (ImageView) view.findViewById(R.id.av_user);
            tvName = (TextView) view.findViewById(R.id.tv_user_name);
            tvMoney = (TextView) view.findViewById(R.id.tv_money);




            relativeLayout = (RelativeLayout) view.findViewById(R.id.chong_money);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), CheckConsumptionRecordsActivity.class));
                }
            });


            rlMe = (RelativeLayout) view.findViewById(R.id.linear_me);
            rlMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                }
            });

            getCurrentUser = new SubscriberOnNextListener<Seller>() {
                @Override
                public void onNext(Seller seller) {
                    OnlineUserInfo.seller = seller;
                    setSeller(seller);
                }
            };

        }
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }

    /**
     * 获取当前用户信息
     */
    public void getUser() {
        HttpMethods.getInstance().getCurrentUser(new ProgressSubscriber(getCurrentUser, activity, false));
    }

    public void setSeller(Seller Seller) {
        Picasso.with(activity).load(HttpMethods.BASE_URL + Seller.getAvatar()).error(R.drawable.unknow_avatar).into(av);
        tvName.setText(seller.getName());
        tvMoney.setText(Seller.getCoin() + "");

    }


    @OnClick({R.id.linearLayout_goods_edit, R.id.linearLayout_info_edit, R.id.linearLayout_picture_edit, R.id.linearLayout_post_edit, R.id.linearLayout0})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.linearLayout_goods_edit:
                intent = new Intent(activity, SellerGoodsEditActivity.class);
                break;
            case R.id.linearLayout_info_edit:
                intent = new Intent(activity, SellerInfoEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Seller", seller);
                intent.putExtras(bundle);

                break;
            case R.id.linearLayout_picture_edit:
                break;
            case R.id.linearLayout_post_edit:
                intent = new Intent(activity, SellerPostctivity.class);
                break;
            case R.id.linearLayout0:
                intent=new Intent(activity, OrdersCommenActivity.class);

                break;
        }
        if (intent != null)
            startActivity(intent);
    }


}
