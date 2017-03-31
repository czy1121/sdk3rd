package ezy.sdk3rd.social.sdk;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ezy on 17/3/18.
 */

public class Platform {
    final String name;
    final String appId;

    Map<String, String> extra;

    public Platform(@NonNull String name, @NonNull String appId) {
        this.name = name;
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public String getAppId() {
        return appId;
    }

    public String extra(String key) {
        return extra == null ? "" : extra.get(key);
    }

    public Platform extra(String key, String value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }
}
