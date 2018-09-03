package com.oraclechain.pe4devs.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by pocketEos on 2017/11/23.
 */
public class JsonUtil {
    /**
     * The constant jsonUtils.
     */
    public static JsonUtil jsonUtils;

    /**
     * Instantiates a new Json util.
     */
    public JsonUtil() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static JsonUtil getInstance() {
        if (jsonUtils == null) {
            synchronized (JsonUtil.class) {
                if (jsonUtils == null) {
                    jsonUtils = new JsonUtil();
                }
            }
        }
        return jsonUtils;
    }

    /**
     * Parse string to bean object.
     *
     * @param str   the str
     * @param clazz the clazz
     * @return the object
     */
    public static Object parseStringToBean(String str, Class clazz) {
        Object object = null;
        try {
            Gson gson = new Gson();
            object = gson.fromJson(str, clazz);
        } catch (JsonSyntaxException e) {
            ToastUtils.showShortToast("数据解析异常");
        }
        return object;
    }

    /**
     * Parse json to array list array list.
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the array list
     */
    public static <T> ArrayList<T> parseJsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            }
            else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            }
            else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            }
            else if (c == ':') {
                jsonFormat.append(c + " ");
            }
            else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                }
                else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            }
            else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                }
                else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            }
            else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    // 是空格还是tab
    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }
}

