package ezy.sdk3rd.social.sdk;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;

/**
 * Created by ezy on 17/3/18.
 */

public class DefaultFactory<T extends IResult> implements IFactory<T> {
    final Platform platform;

    final Class<T> clazz;

    public DefaultFactory(@NonNull String name, String appId, Class<T> clazz) {
        this.platform = new Platform(name, appId);
        this.clazz = clazz;
    }

    public DefaultFactory(@NonNull Platform platform, Class<T> clazz) {
        this.platform = platform;
        this.clazz = clazz;
    }

    @Override
    public Platform getPlatform() {
        return platform;
    }

    @Override
    public T create(Activity activity) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(Activity.class, Platform.class);
            constructor.setAccessible(true);
            return constructor.newInstance(activity, platform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
