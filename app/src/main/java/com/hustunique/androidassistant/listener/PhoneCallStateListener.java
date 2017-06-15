package com.hustunique.androidassistant.listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.hustunique.androidassistant.db.BlackList;
import com.hustunique.androidassistant.db.BlockedCallSaver;
import com.hustunique.androidassistant.util.LogUtil;

import java.lang.reflect.Method;

/**
 * Created by sunpe on 2017/6/13.
 */

public class PhoneCallStateListener extends PhoneStateListener {
    private Context context;
    private BlackList mBlackList;
    private BlockedCallSaver mBlockedCallSaver;
    private static final String TAG = "CallListener";

    public PhoneCallStateListener(Context context){
        this.context = context;
        mBlackList = new BlackList(context);
        mBlockedCallSaver = new BlockedCallSaver(context);
    }


    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);

        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:

                LogUtil.d(TAG, "a call incoming");
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //Turn ON the mute
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Class clazz = Class.forName(telephonyManager.getClass().getName());
                    Method method = clazz.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
                    //Checking incoming call number
                    LogUtil.d(TAG, "incoming number: " + incomingNumber);

                    if (mBlackList.ifNumberInBlackList(incomingNumber)) {
                        mBlockedCallSaver.addBlockedCall(incomingNumber);
                        //telephonyService.silenceRinger();//Security exception problem
                        telephonyService = (ITelephony) method.invoke(telephonyManager);
                        telephonyService.silenceRinger();
                        LogUtil.d(TAG, "blocked");
                        telephonyService.endCall();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                //Turn OFF the mute
//                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_ALLOW_RINGER_MODES);
                break;
            case PhoneStateListener.LISTEN_CALL_STATE:

        }
        super.onCallStateChanged(state, incomingNumber);
    }
}
