package com.oraclechain.pe4devs.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.util.KeyBoardUtil;
import com.oraclechain.pe4devs.view.webview.BaseWebSetting;
import com.oraclechain.pe4devs.view.webview.BaseWebView;

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

    String url = null;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.close)
    TextView mClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp);
        ButterKnife.bind(this);

        // 开启辅助功能崩溃
        mWebDappDetails.disableAccessibility(this);
        new BaseWebSetting(mWebDappDetails, DappActivity.this, false);//设置webseeting
        mWebDappDetails.getSettings().setUserAgentString("PocketEosAndroid");
        mWebDappDetails.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (error.getPrimaryError() == SslError.SSL_INVALID) {
                    handler.proceed();
                } else {
                    handler.cancel();
                }
            }
        });
        mWebDappDetails.getSettings().setJavaScriptEnabled(true);
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
        url = "http://" + mUrl.getText().toString();
        if (KeyBoardUtil.isSoftInputShow(DappActivity.this)) {
            KeyBoardUtil.getInstance(DappActivity.this).hide();
        }
        mWebDappDetails.loadUrl(url);
        mWebDappDetails.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    mWebDappDetails.loadUrl("javascript:getEosAccount('" + getIntent().getStringExtra("account") + "')");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(progress);//设置进度值
                }
            }
        });
    }

}
