package hu.ait.android.shoppinglist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by alicetan on 11/7/17.
 */

public class TobuyApplication extends Application {

    private Realm realmTobuy;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmTobuy = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmTobuy.close();
    }

    public Realm getRealmItems() {
        return realmTobuy;
    }
}
