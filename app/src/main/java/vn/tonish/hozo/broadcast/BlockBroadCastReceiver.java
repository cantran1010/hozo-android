package vn.tonish.hozo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 6/15/17.
 */

public class BlockBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = BlockBroadCastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "BlockBroadCastReceiver onReceive");
        Utils.blockUser(context);
    }

}
