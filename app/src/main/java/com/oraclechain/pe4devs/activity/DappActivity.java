package com.oraclechain.pe4devs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.util.KeyBoardUtil;
import com.oraclechain.pe4devs.view.webview.BaseWebChromeClient;
import com.oraclechain.pe4devs.view.webview.BaseWebSetting;
import com.oraclechain.pe4devs.view.webview.BaseWebView;
import com.oraclechain.pe4devs.view.webview.BaseWebViewClient;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DappActivity extends AppCompatActivity {

    @BindView(R.id.url)
    EditText mUrl;
    @BindView(R.id.go)
    Button mGo;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_dapp_details)
    BaseWebView mWebDappDetails;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.close)
    TextView mClose;
    @BindView(R.id.title)
    TextView mTitle;


    String url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp);
        ButterKnife.bind(this);

        // 开启辅助功能崩溃
        mWebDappDetails.disableAccessibility(DappActivity.this);
        new BaseWebSetting(mWebDappDetails, DappActivity.this, false);//设置webseeting
        mWebDappDetails.setWebViewClient(new BaseWebViewClient(this));
        mWebDappDetails.getSettings().setUserAgentString("PocketEosAndroid");
        mWebDappDetails.addJavascriptInterface(new DappInterface(mWebDappDetails, this), "DappJsBridge");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebDappDetails.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                    mWebDappDetails.goBack();
                } else {//当webview处于第一页面时,直接退出程序
                    finish();
                }
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebDappDetails.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                mWebDappDetails.goBack();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.go)
    public void onViewClicked() {
        mWebDappDetails.clearCache(true);
        url = "http://" + mUrl.getText().toString();
        if (KeyBoardUtil.isSoftInputShow(DappActivity.this)) {
            KeyBoardUtil.getInstance(DappActivity.this).hide();
        }
        mWebDappDetails.loadUrl(url);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("wallet_name", MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getName());
        hashMap.put("uid", MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getUid());
        hashMap.put("image", MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getImage());
        hashMap.put("account", getIntent().getStringExtra("account"));
        mWebDappDetails.setWebChromeClient(new BaseWebChromeClient(this, mProgressBar, mTitle,new Gson().toJson(hashMap).toString() ,getIntent().getStringExtra("account")));
    }

}
