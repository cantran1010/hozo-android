package vn.tonish.hozo.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.share.LinkGenerator;
import com.appsflyer.share.ShareInviteHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.accountkit.AccountKit;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import vn.tonish.hozo.BuildConfig;
import vn.tonish.hozo.database.manager.RealmDbHelper;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.TypefaceContainer;


public class HozoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //noinspection deprecation
        AccountKit.initialize(getApplicationContext());
        Realm.init(this);
        Realm.setDefaultConfiguration(RealmDbHelper.getRealmConfig(getApplicationContext()));

        TypefaceContainer.init(getApplicationContext());

        //fix bug capture image on s8
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!BuildConfig.DEBUG) {
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);

            if (UserManager.checkLogin())
                Crashlytics.setInt("user_id", UserManager.getMyUser().getId());
        }

        try {
            //add appflyer
            AppsFlyerLib.getInstance().startTracking(this, "Lx3D92KPaWNC9hTKciGEtD");
            AppsFlyerLib.getInstance().setAppInviteOneLink("hozo_invite_1");
            LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(this);

            linkGenerator.addParameter("user1", "name1");
            linkGenerator.addParameter("user2", "name2");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
