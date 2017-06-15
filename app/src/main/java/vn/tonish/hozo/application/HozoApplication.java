package vn.tonish.hozo.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import io.realm.Realm;
import vn.tonish.hozo.database.manager.RealmDbHelper;
import vn.tonish.hozo.utils.TypefaceContainer;


public class HozoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Realm.setDefaultConfiguration(RealmDbHelper.getRealmConfig(getApplicationContext()));

        TypefaceContainer.init(getApplicationContext());

        //fix bug capture image on s8
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)
//                .build();
//        Fabric.with(fabric);
//
//        if (UserManager.checkLogin())
//            Crashlytics.setInt("user_id", UserManager.getMyUser().getId());

//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/NanumBarunGothic.ttf");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
