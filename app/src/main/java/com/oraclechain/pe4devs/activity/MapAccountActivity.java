package com.oraclechain.pe4devs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.app.BaseUrl;
import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.bean.AccountBean;
import com.oraclechain.pe4devs.bean.GetAccountsBean;
import com.oraclechain.pe4devs.blockchain.cypto.ec.EosPrivateKey;
import com.oraclechain.pe4devs.net.HttpUtils;
import com.oraclechain.pe4devs.net.callbck.JsonCallback;
import com.oraclechain.pe4devs.util.EncryptUtil;
import com.oraclechain.pe4devs.util.PasswordToKeyUtils;
import com.oraclechain.pe4devs.util.ShowDialog;
import com.oraclechain.pe4devs.util.ToastUtils;
import com.oraclechain.pe4devs.view.ClearEditText;
import com.oraclechain.pe4devs.view.dialog.passworddialog.PasswordCallback;
import com.oraclechain.pe4devs.view.dialog.passworddialog.PasswordDialog;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//导入映射账号
public class MapAccountActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.active_private_key)
    ClearEditText mActivePrivateKey;
    @BindView(R.id.import_number)
    TextView mImportNumber;

    private String mAccount_owner_private_key, mAccount_active_private_key = null;
    private String mAccount_owner_public_key, mAccount_active_public_key = null;
    private String userPassword, accountName = null;
    private List<AccountBean> mAccountBeanList = MyApplication.getDaoInstant().getAccountBeanDao().loadAll();
    private EosPrivateKey mActiveKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_account);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.import_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.import_number:

                if (!TextUtils.isEmpty(mActivePrivateKey.getText().toString())) {
                    PasswordDialog dialog = new PasswordDialog(MapAccountActivity.this, new PasswordCallback() {
                        @Override
                        public void sure(String password) {
                            if (mAccountBeanList.get(0).getSha_pwd().equals(PasswordToKeyUtils.shaCheck(password))) {
                                userPassword = password;
                                ShowDialog.showDialog(MapAccountActivity.this, "", false, null);
                                try {
                                    mActiveKey = new EosPrivateKey(mActivePrivateKey.getText().toString().trim());
                                    mAccount_owner_public_key = mActiveKey.getPublicKey().toString();
                                    mAccount_active_public_key = mActiveKey.getPublicKey().toString();
                                    mAccount_active_private_key = mActiveKey.toString();
                                    mAccount_owner_private_key = mActiveKey.toString();
                                    getAccountInfoData(mAccount_active_public_key);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ShowDialog.dissmiss();
                                    ToastUtils.showShortToast("私钥格式错误");
                                }

                            } else {
                                ToastUtils.showShortToast("密码输入错误");
                            }
                        }

                        @Override
                        public void cancle() {
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    ToastUtils.showShortToast("请输入您的私钥");
                }
                break;
        }
    }


    public void getAccountInfoData(String public_key) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("public_key", public_key);
        HttpUtils.postRequest(BaseUrl.getHTTP_GetAccounts, this, new JSONObject(hashMap).toString(), new JsonCallback<GetAccountsBean>() {
            @Override
            public void onSuccess(Response<GetAccountsBean> response) {
                ShowDialog.dissmiss();
                if (response.body().getCode().equals("0") && response.body().getData().getAccount_names().size() != 0) {
                    accountName = response.body().getData().getAccount_names().get(0);

                    for (int i = 0; i < mAccountBeanList.size(); i++) {
                        if (mAccountBeanList.get(i).getAccount() != null) {
                            if (mAccountBeanList.get(i).getAccount().equals(accountName)) {
                                ToastUtils.showShortToast("您已拥有该账号,请勿重复导入");
                                return;
                            }
                        }
                    }
                    ToastUtils.showShortToast("导入账号成功");
                    if (mAccountBeanList.size() == 1 && TextUtils.isEmpty(mAccountBeanList.get(0).getAccount())) {
                        AccountBean accountBean = mAccountBeanList.get(0);
                        accountBean.setAccount(accountName);
                        accountBean.setActive_public(mAccount_active_public_key);
                        accountBean.setOwner_public(mAccount_owner_public_key);
                        try {
                            accountBean.setActive_pravite(EncryptUtil.getEncryptString(mAccount_active_private_key, userPassword));
                            accountBean.setOwner_pravite(EncryptUtil.getEncryptString(mAccount_owner_private_key, userPassword));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                        MyApplication.getDaoInstant().getAccountBeanDao().update(accountBean);
                    } else {
                        AccountBean accountBean = new AccountBean();
                        accountBean.setAccount(accountName);
                        accountBean.setActive_public(mAccount_active_public_key);
                        accountBean.setOwner_public(mAccount_owner_public_key);
                        accountBean.setName(mAccountBeanList.get(0).getName());
                        accountBean.setImage(mAccountBeanList.get(0).getImage());
                        accountBean.setPhone(mAccountBeanList.get(0).getPhone());
                        accountBean.setUid(mAccountBeanList.get(0).getUid());
                        accountBean.setSha_pwd(mAccountBeanList.get(0).getSha_pwd());
                        try {
                            accountBean.setActive_pravite(EncryptUtil.getEncryptString(mAccount_active_private_key, userPassword));
                            accountBean.setOwner_pravite(EncryptUtil.getEncryptString(mAccount_owner_private_key, userPassword));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                        MyApplication.getDaoInstant().getAccountBeanDao().insert(accountBean);
                    }
                    finish();
                } else if (response.body().getCode().equals("0") && response.body().getData().getAccount_names().size() == 0) {
                    ToastUtils.showShortToast("暂无账号");
                } else {
                    ToastUtils.showShortToast(response.body().getMsg());
                }
            }
        });


    }
}
