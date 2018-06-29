package com.oraclechain.pe4devs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.app.ActivityUtils;
import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.bean.AccountBean;
import com.oraclechain.pe4devs.util.ToastUtils;
import com.oraclechain.pe4devs.view.RecycleViewDivider;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.set_pwd)
    TextView mSetPwd;
    @BindView(R.id.import_map_account)
    TextView mImportMapAccount;
    @BindView(R.id.import_account)
    TextView mImportAccount;
    @BindView(R.id.account_list)
    RecyclerView mAccountList;

    private List<AccountBean> mAccountInfoBeanList = new ArrayList<>();
    private EmptyWrapper mAccountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().size() > 0) {
            mSetPwd.setVisibility(View.GONE);
        } else {
            mSetPwd.setVisibility(View.VISIBLE);
        }


    }

    @OnClick({R.id.set_pwd, R.id.import_map_account, R.id.import_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_pwd:
                ActivityUtils.next(this, SetPasswordActivity.class);
                break;
            case R.id.import_map_account:
                if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().size() == 0) {
                    ToastUtils.showShortToast("请优先设置密码~");
                    return;
                } else {
                    ActivityUtils.next(this, MapAccountActivity.class);
                }

                break;
            case R.id.import_account:
                if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().size() == 0) {
                    ToastUtils.showShortToast("请优先设置密码~");
                    return;
                } else {
                    ActivityUtils.next(this, MapAccountActivity.class);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加账号之后进行数据刷新~
        if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().size() > 0) {
            mSetPwd.setVisibility(View.GONE);
        } else {
            mSetPwd.setVisibility(View.VISIBLE);
        }
        mAccountInfoBeanList = MyApplication.getDaoInstant().getAccountBeanDao().loadAll();
        if (mAccountInfoBeanList.size() != 0) {
            if (TextUtils.isEmpty(mAccountInfoBeanList.get(0).getAccount())) {
                mAccountInfoBeanList.clear();
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        mAccountList.setLayoutManager(layoutManager);
        mAccountList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line)));
        mAccountAdapter = new EmptyWrapper(new CommonAdapter<AccountBean>(this, R.layout.item_account, mAccountInfoBeanList) {
            @Override
            protected void convert(ViewHolder holder, final AccountBean accountInfoBean, final int position) {
                TextView account_number = holder.getView(R.id.account_number);
                account_number.setText(accountInfoBean.getAccount());
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(accountInfoBean.getAccount())) {
                            Bundle bundle = new Bundle();
                            bundle.putString("account", accountInfoBean.getAccount());
                            ActivityUtils.next(MainActivity.this, DappActivity.class, bundle);
                        }
                    }
                });
            }
        });
        mAccountAdapter.setEmptyView(R.layout.empty_project);
        mAccountList.setAdapter(mAccountAdapter);
    }
}
