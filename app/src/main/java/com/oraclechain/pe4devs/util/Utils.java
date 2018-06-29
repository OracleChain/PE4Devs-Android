package com.oraclechain.pe4devs.util;

import android.content.Context;

/**
 * Created by pocketEos on 2017/11/23.
 * Utils初始化相关
 */
public final class Utils {

    private static Context context;



    private Utils() {
        throw new UnsupportedOperationException("...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext context
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("应该首先初始化");
    }

}
