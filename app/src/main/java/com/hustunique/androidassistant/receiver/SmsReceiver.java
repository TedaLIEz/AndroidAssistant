package com.hustunique.androidassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.hustunique.androidassistant.db.AutoBlockListManager;
import com.hustunique.androidassistant.db.BlackList;
import com.hustunique.androidassistant.db.BlockedSMSSaver;
import com.hustunique.androidassistant.manager.PrefManager;
import com.hustunique.androidassistant.util.LogUtil;

/**
 * Created by sunpe on 2017/6/14.
 */

public class SmsReceiver extends BroadcastReceiver {
    /** Called when the activity is first created. */
    public static int MSG_TPE=0;
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (!PrefManager.getInstance().getDefaultPreferences(context).getBoolean("BlockEnable", false)){
            return;
        }
        BlockedSMSSaver smsSaver = new BlockedSMSSaver();
        BlackList blackList = new BlackList();
        AutoBlockListManager autoBlockList = new AutoBlockListManager();

        String MSG_TYPE=intent.getAction();
        LogUtil.d(TAG, "SMS Received: " + MSG_TYPE);
        if (MSG_TYPE.equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++)
            {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }

            LogUtil.d(TAG, "msg src: " + smsMessage[0].getOriginatingAddress());
            LogUtil.d(TAG, "msg body: " + smsMessage[0].getMessageBody());
            String incomingnumber = smsMessage[0].getOriginatingAddress().substring(3);
            boolean inBlackList = blackList.ifNumberInBlackList(incomingnumber);
            boolean inAutoBlock = autoBlockList.ifNumberInAutoBlockList(incomingnumber);
            if (inBlackList || inAutoBlock) {
                smsSaver.addBlockedSMS(smsMessage[0].getOriginatingAddress().substring(3),
                        smsMessage[0].getMessageBody(), inAutoBlock);
            }
        }

    }
}

