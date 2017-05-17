package vn.tonish.hozo.application;

import android.app.Application;
import android.content.Context;
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

//        Fabric.with(this, new Crashlytics());

//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)
//                .build();
//        Fabric.with(fabric);

//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/NanumBarunGothic.ttf");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
