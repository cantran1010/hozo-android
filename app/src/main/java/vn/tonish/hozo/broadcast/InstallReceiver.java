package vn.tonish.hozo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.analytics.AnalyticsReceiver;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static vn.tonish.hozo.utils.PreferUtils.setReferrerId;
import static vn.tonish.hozo.utils.Utils.getHashMapFromQuery;


public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String rawReferrer = intent.getStringExtra("referrer");
        if (rawReferrer != null) {
            try {
                Map<String, String> getParams = getHashMapFromQuery(rawReferrer);
                String referrerID = getParams.get("referrer_id");
                if (referrerID != null) setReferrerId(context, Integer.parseInt(referrerID));
            } catch (UnsupportedEncodingException e) {
                Log.e("Referrer Error", e.getMessage());
            }

        }

        AnalyticsReceiver receiver = new AnalyticsReceiver();
        receiver.onReceive(context, intent);
    }
}

