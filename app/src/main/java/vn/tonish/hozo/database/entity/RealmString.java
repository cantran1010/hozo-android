package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by CanTran on 9/23/17.
 */

public class RealmString extends RealmObject {
    private String val;

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return "RealmString{" +
                "val='" + val + '\'' +
                '}';
    }
}
