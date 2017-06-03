package com.orsomob.coordinates;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by LucasOrso on 6/2/17.
 */

public class MainApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
