package com.reelify.kkkkwillo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.ListInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MySettings {
    private static final String SETTING_INFO = "setting_info";
    private static MySettings settings;
    private Context context;

    private MySettings(Context context) {
        this.context = context;
    }

    public static synchronized void init(Context context) {
        if (settings == null && context != null) {
            settings = new MySettings(context.getApplicationContext());
        }
    }

    public static MySettings getInstance() {
        return settings;
    }

    public String getStringSetting(String SettingName) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(SETTING_INFO,
                Context.MODE_PRIVATE).getString(SettingName, "");
    }

    public int getIntSetting(String SettingName) {
        if (context == null) {
            return -1;
        }
        return context.getSharedPreferences(SETTING_INFO,
                Context.MODE_PRIVATE).getInt(SettingName, 0);
    }

    public boolean getBooleanSetting(String SettingName) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(SETTING_INFO,
                Context.MODE_PRIVATE).getBoolean(SettingName, false);
    }

    public boolean saveSetting(String settingName, boolean settingValue) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putBoolean(settingName, settingValue);
        return editor.commit();
    }

    public boolean saveSetting(String settingName, int intValue) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putInt(settingName, intValue);
        return editor.commit();
    }



    public boolean saveSetting(String settingName, long intValue) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putLong(settingName, intValue);
        return editor.commit();
    }

    public long getLongSetting(String SettingName) {
        if (context == null) {
            return -1;
        }
        return context.getSharedPreferences(SETTING_INFO,
                Context.MODE_PRIVATE).getLong(SettingName, -1);
    }


    public boolean saveSetting(String settingName, String settingValue) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, settingValue);
        return editor.commit();
    }
    public boolean saveSetting(String settingName, ListInfo settingValue) {
        if (context == null) {
            return false;
        }
        Gson gson = new Gson();
        String jsonStr=gson.toJson(settingValue);
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, jsonStr);
        return editor.commit();
    }

    public boolean saveSetting(String settingName, List<String> intValue) {
        if (context == null) {
            return false;
        }
        Gson gson = new Gson();
        String jsonStr=gson.toJson(intValue);
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, jsonStr);
        return editor.commit();
    }

    public boolean saveSetting(String settingName, ConfigInfo settingValue) {
        if (context == null) {
            return false;
        }
        Gson gson = new Gson();
        String jsonStr=gson.toJson(settingValue);
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, jsonStr);
        return editor.commit();
    }

    public boolean remove(String key) {
        if (context == null || key == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        return editor.commit();
    }

    public static List<ListInfo.Data> sortInfo(List<ListInfo.Data> list) {
        Comparator<Object> cmp = Collator.getInstance(Locale.ENGLISH);
        list.sort(((o1, o2) ->cmp.compare(o2.type,o1.type)));
        return list;
    }

    public static List<ListInfo.Data> removeInfo(List<ListInfo.Data> list) {
        list.removeIf(next -> next.getType().equalsIgnoreCase("c"));
        return list;
    }

    public void saveSetting(String comments, ListInfo.Comment comments1) {
        if (context == null) {
            return;
        }
        Gson gson = new Gson();
        String jsonStr=gson.toJson(comments1);
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(comments, jsonStr);
        editor.apply();
    }
}
