package com.oraclechain.pe4devs.view.dialog.dappactiondialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.blockchain.chain.Action;
import com.oraclechain.pe4devs.util.DensityUtil;
import com.oraclechain.pe4devs.view.convenientbanner.ConvenientBanner;
import com.oraclechain.pe4devs.view.convenientbanner.adapter.ActionHolderView;
import com.oraclechain.pe4devs.view.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pocketEos on 2017/12/11.
 * Dapp请求签名action信息展示
 */

public class DappActionDialog extends Dialog implements View.OnClickListener {

    DappActionCallBack callback;
    private TextView close;
    private ConvenientBanner action_details;
    private EditText password;
    private Button go_sign;
    private LinearLayout ll;
    private Context context;
    private List<Action> mActionList = new ArrayList<>();


    public DappActionDialog(Context context, DappActionCallBack callback) {
        super(context, R.style.PhotoDialog);
        this.callback = callback;
        this.context = context;
        setCustomDialog();
    }

    private void setCustomDialog() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_dapp_action, null);

        close = (TextView) mView.findViewById(R.id.close);
        action_details = (ConvenientBanner) mView.findViewById(R.id.action_details);
        go_sign = (Button) mView.findViewById(R.id.go_sign);
        password = (EditText) mView.findViewById(R.id.password);
        ll = (LinearLayout) mView.findViewById(R.id.ll);
        close.setOnClickListener(this);
        go_sign.setOnClickListener(this);
        ll.setOnClickListener(this);

        super.setContentView(mView);

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        lp.height = (int) (display.getHeight() - DensityUtil.getDaoHangHeight(context)); //设置宽度
        this.getWindow().setAttributes(lp);
    }


    public DappActionDialog setContent(List<Action> actionArrayList) {
        go_sign.setClickable(false);
        mActionList = actionArrayList;
        action_details.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ActionHolderView();
            }
        }, mActionList).setPageIndicator(new int[]{
                R.drawable.gary_point, R.drawable.blue_point})//设置小圆点
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        if (mActionList.size() == 1) {
            go_sign.setBackgroundColor(context.getResources().getColor(R.color.blue_button));
            go_sign.setClickable(true);
        }
        action_details.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == (mActionList.size() - 1)) {
                    go_sign.setBackgroundColor(context.getResources().getColor(R.color.blue_button));
                    go_sign.setClickable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll:
                this.cancel();
                callback.cancle();
                break;
            case R.id.close:
                this.cancel();
                callback.cancle();
                break;
            case R.id.go_sign:
                if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    password.setVisibility(View.VISIBLE);
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);
                    password.requestFocus();
                    action_details.setVisibility(View.VISIBLE);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }, 200); // 0.2秒后自动弹出
                } else {
                    callback.goTraction(password.getText().toString().trim());
                }
                break;
        }
    }

}
