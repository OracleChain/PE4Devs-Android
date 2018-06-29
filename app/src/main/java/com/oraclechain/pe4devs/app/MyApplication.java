package com.oraclechain.pe4devs.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.oraclechain.pe4devs.gen.DaoMaster;
import com.oraclechain.pe4devs.gen.DaoSession;
import com.oraclechain.pe4devs.util.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by pocketEos on 2017/11/23.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    private static DaoSession daoSession;
    public static MyApplication getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        try {
            initOkGo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //配置数据库
        setupDatabase();

        Utils.init(getApplicationContext());
    }
    /**
     * 分割 Dex 支持
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mInstance != null) {
            mInstance = null;
        }
    }

    public void initOkGo() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        if (new SPCookieStore(this).getAllCookie().size() != 0) {
            headers.put("cookie", String.valueOf(new SPCookieStore(this).getAllCookie().get(0)));
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkHttp");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效


        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("server.cer"));
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        //配置https的域名匹配规则，使用不当会导致https握手失败
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        // 其他统一的配置
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //必须设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);               //全局公共头
//                .addCommonParams(params);                       //全局公共参数

    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoInstant() {
        return daoSession;
    }

}

