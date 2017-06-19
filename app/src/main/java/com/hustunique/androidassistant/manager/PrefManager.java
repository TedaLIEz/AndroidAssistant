package com.hustunique.androidassistant.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sunpe on 2017/6/19.
 */

public class PrefManager {
    private static PrefManager sInstance;
    public static PrefManager getInstance() {
        if (sInstance == null) {
            sInstance = new PrefManager();
        }
        return sInstance;
    }


    private PrefManager() {

    }

    public Pref getDefaultPreferences(Context context) {
        return new Pref(context);
    }

    public class Pref {
        SharedPreferences pref;
        public Pref(Context context) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        public PrefEditor edit() {
            return new PrefEditor(pref.edit());
        }

        public boolean getBoolean(String s, boolean dft) {
            return pref.getBoolean(s, dft);
        }
    }

    public class PrefEditor {
        SharedPreferences.Editor editor;
        public PrefEditor(SharedPreferences.Editor editor){
            this.editor = editor;
        }

        public PrefEditor putBoolean(String var1, boolean var2) {
            editor.putBoolean(var1, var2);
            return this;
        }

        public PrefEditor apply() {
            editor.apply();
            return this;
        }
    }
}
