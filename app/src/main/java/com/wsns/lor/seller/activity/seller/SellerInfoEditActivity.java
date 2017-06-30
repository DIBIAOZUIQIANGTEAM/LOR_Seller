package com.wsns.lor.seller.activity.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.application.OnlineUserInfo;
import com.wsns.lor.seller.entity.Seller;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wsns.lor.seller.application.OnlineUserInfo.seller;

/**
 * 商家信息编辑界面
 */
public class SellerInfoEditActivity extends Activity {


    @BindView(R.id.jmui_commit_btn)
    Button sellerCommitBtn;

    SubscriberOnNextListener updatesellerOnNext;
    @BindView(R.id.return_btn)
    ImageButton returnBtn;
    @BindView(R.id.et_category)
    EditText etCategory;
    @BindView(R.id.et_address)
    TextView etAddress;
    @BindView(R.id.et_hotline)
    EditText etHotline;
    @BindView(R.id.et_worktime)
    EditText etWorktime;
    @BindView(R.id.et_service)
    EditText etService;

    @BindView(R.id.tv_notice)
    EditText tvNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerinfo_edit);
        ButterKnife.bind(this);

        etService.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        etCategory.setText(seller.getRepairsTypes());
        tvNotice.setText(seller.getNotice());
        etService.setText(seller.getService() + "");
        etHotline.setText(seller.getHotline());
        etWorktime.setText(seller.getWorktime());

        updatesellerOnNext = new SubscriberOnNextListener<Seller>() {
            @Override
            public void onNext(Seller seller) {
                OnlineUserInfo.seller = seller;
                ToastUtil.show(SellerInfoEditActivity.this, "修改成功");
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();

        etAddress.setText(seller.getAddress());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.jmui_commit_btn, R.id.et_address, R.id.return_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jmui_commit_btn:
                seller.setRepairsTypes(etCategory.getText().toString());
                seller.setHotline(etHotline.getText().toString());
                seller.setWorktime(etWorktime.getText().toString());
                seller.setService(Double.valueOf(etService.getText().toString()));
                seller.setNotice(tvNotice.getText().toString());

                HttpMethods.getInstance().updateSeller(new ProgressSubscriber(updatesellerOnNext, this, true),
                        seller);
                break;
            case R.id.et_address:
                Intent intent = new Intent(this, SellerAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.return_btn:
                finish();
                break;


        }
    }

    private void setPaywayPopupView(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        View popupView = getLayoutInflater().inflate(R.layout.dialog_trade_way, null);

        final PopupWindow mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.mystyle);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -200);
    }
}
