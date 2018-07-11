package com.oraclechain.pe4devs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.bean.AccountBean;
import com.oraclechain.pe4devs.util.EncryptUtil;
import com.oraclechain.pe4devs.util.MD5Util;
import com.oraclechain.pe4devs.util.PasswordToKeyUtils;
import com.oraclechain.pe4devs.util.ToastUtils;
import com.oraclechain.pe4devs.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetWalletInfoActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.password)
    ClearEditText mPassword;
    @BindView(R.id.confirm_password)
    ClearEditText mConfirmPassword;
    @BindView(R.id.create_wallet)
    TextView mCreateWallet;
    @BindView(R.id.name)
    ClearEditText mName;
    @BindView(R.id.phone)
    ClearEditText mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.create_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.create_wallet:
                if (TextUtils.isEmpty(mName.getText().toString()) || TextUtils.isEmpty(mPhone.getText().toString()) || TextUtils.isEmpty(mPassword.getText().toString()) || TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
                    ToastUtils.showShortToast("请输入钱包信息~");
                } else if (mPassword.getText().toString() != null && mConfirmPassword.getText().toString() != null && mConfirmPassword.getText().toString().equals(mPassword.getText().toString())) {
                    if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().size() == 0) {
                        AccountBean accountBean = new AccountBean();
                        accountBean.setAccount(null);
                        String randomString = EncryptUtil.getRandomString(32);
                        accountBean.setSha_pwd(PasswordToKeyUtils.shaEncrypt(randomString + mPassword.getText().toString().trim()));
                        accountBean.setName(mName.getText().toString());
                        accountBean.setImage("https://pocketeos.oss-cn-beijing.aliyuncs.com/person_default_img.png");
                        accountBean.setPhone(mPhone.getText().toString());
                        accountBean.setUid(MD5Util.getMD5String(mPhone.getText().toString()));
                        MyApplication.getDaoInstant().getAccountBeanDao().insert(accountBean);
                        finish();
                    } else {
                        ToastUtils.showShortToast("您两次输入的密码不一致");
                    }
                }
                break;
        }
    }
}
