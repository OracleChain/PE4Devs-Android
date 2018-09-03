package com.oraclechain.pe4devs.activity;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.lzy.okgo.utils.OkLogger;
import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.bean.PushActionsBean;
import com.oraclechain.pe4devs.blockchain.DappDatamanger;
import com.oraclechain.pe4devs.blockchain.chain.Action;
import com.oraclechain.pe4devs.util.JsonUtil;
import com.oraclechain.pe4devs.util.PasswordToKeyUtils;
import com.oraclechain.pe4devs.util.ShowDialog;
import com.oraclechain.pe4devs.util.ToastUtils;
import com.oraclechain.pe4devs.view.dialog.dappactiondialog.DappActionCallBack;
import com.oraclechain.pe4devs.view.dialog.dappactiondialog.DappActionDialog;
import com.oraclechain.pe4devs.view.dialog.passworddialog.PasswordCallback;
import com.oraclechain.pe4devs.view.dialog.passworddialog.PasswordDialog;
import com.oraclechain.pe4devs.view.webview.BaseWebView;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

/**
 * Created by pocketEos on 2018/4/25.
 */
public class DappInterface {

    private Context mContext;
    private BaseWebView mBaseWebView;
    DappActionDialog dappActionDialog;
    /**
     * Instantiates a new Dapp interface.
     */
    public DappInterface(BaseWebView baseWebView, Context context) {
        this.mBaseWebView = baseWebView;
        mContext = context;
    }

    /**
     * Push action string.
     *
     * @param message           the message
     * @param permissionAccount the permission account
     * @return the string
     */
    @JavascriptInterface
    public void pushAction(final String serialNumber, final String message, final String permissionAccount) {
        OkLogger.i("============>message" + message);
        OkLogger.i("============>serialNumber" + serialNumber);
        OkLogger.i("============>permissionAccount" + permissionAccount);
        PasswordDialog mPasswordDialog = new PasswordDialog(mContext, new PasswordCallback() {
            @Override
            public void sure(String password) {
                if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getSha_pwd().equals(PasswordToKeyUtils.shaCheck(password))) {
                    ShowDialog.showDialog(mContext, "", true, null).show();
                    new DappDatamanger(mContext, password, new DappDatamanger.Callback() {
                        @Override
                        public void getTxid(final String txId) {
                            OkLogger.i("=================>" + txId);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + txId + "')");
                                }
                            });
                        }

                        @Override
                        public void erroMsg(String msg) {
                            final String finalMsg = "ERROR:" + msg;
                            OkLogger.i("=================>" + finalMsg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + finalMsg + "')");
                                }
                            });
                        }
                    }).pushAction(message, permissionAccount);
                } else {
                    final String msg = "ERROR:密码错误";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                        }
                    });
                    ToastUtils.showLongToast("密码错误");
                }

            }

            @Override
            public void cancle() {
                final String msg = "ERROR:取消";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                    }
                });
            }
        });
        mPasswordDialog.setCancelable(false);
        mPasswordDialog.show();
    }

    /**
     * Push action string.
     *
     * @param message           the message
     * @param permissionAccount the permission account
     * @return the string
     */
    @JavascriptInterface
    public void push(final String serialNumber, final String contract, final String action, final String message, final String permissionAccount) {
        OkLogger.i("============>message" + message);
        OkLogger.i("============>action" + contract);
        OkLogger.i("============>action" + action);
        OkLogger.i("============>pushTag" + serialNumber);
        OkLogger.i("============>permissionAccount" + permissionAccount);
        PasswordDialog mPasswordDialog = new PasswordDialog(mContext, new PasswordCallback() {
            @Override
            public void sure(String password) {
                if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getSha_pwd().equals(PasswordToKeyUtils.shaCheck(password))) {
                    ShowDialog.showDialog(mContext, "", true, null).show();
                    new DappDatamanger(mContext, password, new DappDatamanger.Callback() {
                        @Override
                        public void getTxid(final String txId) {
                            OkLogger.i("=================>" + txId);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + txId + "')");
                                }
                            });
                        }

                        @Override
                        public void erroMsg(String msg) {
                            final String finalMsg = "ERROR:" + msg;
                            OkLogger.i("=================>" + finalMsg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + finalMsg + "')");
                                }
                            });
                        }
                    }).push(contract, action, message, permissionAccount);
                } else {
                    final String msg = "ERROR:密码错误";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                        }
                    });
                    ToastUtils.showLongToast("密码错误");
                }

            }

            @Override
            public void cancle() {
                final String msg = "ERROR:取消";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                    }
                });
            }
        });
        mPasswordDialog.setCancelable(false);
        mPasswordDialog.show();
    }

    @JavascriptInterface
    public void pushActions(final String serialNumber, final String actionsDetails) {
        final List<Action> mActionList = new ArrayList<>();
        PushActionsBean pushActionsBean = (PushActionsBean) JsonUtil.parseStringToBean(actionsDetails, PushActionsBean.class);
        for (int i = 0; i < pushActionsBean.getActions().size(); i++) {
            Action action = new Action(pushActionsBean.getActions().get(i).getAccount(), pushActionsBean.getActions().get(i).getName());
            action.setAuthorization(new String[]{pushActionsBean.getActions().get(i).getAuthorization().get(0).getActor() + "@active"});
            action.setData(pushActionsBean.getActions().get(i).getData());
            mActionList.add(action);
        }
        dappActionDialog = new DappActionDialog(mContext, new DappActionCallBack() {
            @Override
            public void goTraction(String password) {
                ShowDialog.showDialog(mContext, "", true, null).show();
                if (MyApplication.getDaoInstant().getAccountBeanDao().loadAll().get(0).getSha_pwd().equals(PasswordToKeyUtils.shaCheck(password))) {

                    new DappDatamanger(mContext, password, new DappDatamanger.Callback() {
                        @Override
                        public void getTxid(final String txId) {
                            dappActionDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + txId + "')");
                                }
                            });
                        }

                        @Override
                        public void erroMsg(String msg) {
                            dappActionDialog.dismiss();
                            final String finalMsg = "ERROR:" + msg;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + finalMsg + "')");
                                }
                            });
                        }
                    }).pushActions(mActionList);
                } else {
                    dappActionDialog.dismiss();
                    final String msg = "ERROR:密码错误" ;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                        }
                    });
                    ToastUtils.showLongToast("密码错误");
                }
            }

            @Override
            public void cancle() {
                dappActionDialog.dismiss();
                final String msg = "ERROR:取消" ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBaseWebView.loadUrl("javascript:pushActionResult('" + serialNumber + "','" + msg + "')");
                    }
                });
            }
        });
        dappActionDialog.setContent(mActionList);
        dappActionDialog.setCancelable(false);
        dappActionDialog.show();
    }
}
