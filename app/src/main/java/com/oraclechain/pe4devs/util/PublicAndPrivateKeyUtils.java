package com.oraclechain.pe4devs.util;

import com.oraclechain.pe4devs.app.MyApplication;
import com.oraclechain.pe4devs.bean.AccountBean;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pocketEos on 2018/2/3.
 */

public class PublicAndPrivateKeyUtils {


    //获取钱包下所有账号activepublickey
    public static List<String> getActivePublicKey() {
        List<String> keyList = new ArrayList<>();
        List<AccountBean> mAccountInfoBeanList = MyApplication.getDaoInstant().getAccountBeanDao().loadAll();//遍历本地所有账号信息
        for (int i = 0; i < mAccountInfoBeanList.size(); i++) {
            keyList.add(mAccountInfoBeanList.get(i).getActive_public());
        }
        return keyList;
    }

    //通过公钥获取私钥
    public static String getPrivateKey(String publicKey, String password) {
        String activePrivateKey = null;
        List<AccountBean> mAccountInfoBeanList = MyApplication.getDaoInstant().getAccountBeanDao().loadAll();//遍历本地所有账号信息
        for (int i = 0; i < mAccountInfoBeanList.size(); i++) {
            if (mAccountInfoBeanList.get(i).getActive_public().equals(publicKey)) {
                activePrivateKey = mAccountInfoBeanList.get(i).getActive_pravite();
            }
        }
        if (activePrivateKey != null) {
            String key = null;
            try {
                key = EncryptUtil.getDecryptString(activePrivateKey, password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return key;
        } else {
            return null;
        }
    }

}
