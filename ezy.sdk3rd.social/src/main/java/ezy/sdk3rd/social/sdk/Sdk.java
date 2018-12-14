package ezy.sdk3rd.social.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * Created by ezy on 17/3/18.
 */

public class Sdk<T extends IResult> {

    private Map<String, T> mPlatforms = new HashMap<>();
    private Map<Activity, Map<String, T>> mMap = new WeakHashMap<>();
    private Map<String, IFactory<T>> mFactories = new HashMap<>();

    private OnCallback mDefaultCallback;

    public OnCallback getDefaultCallback() {
        return mDefaultCallback;
    }
    public void setDefaultCallback(OnCallback callback) {
        mDefaultCallback = callback;
    }

    public void register(String name, String appId, Class clazz) {
        mFactories.put(name, new DefaultFactory(name, appId, clazz));
    }

    public void register(IFactory factory) {
        mFactories.put(factory.getPlatform().getName(), factory);
    }

    public boolean isSupport(String platform) {
        return mFactories.get(platform) != null;
    }

    public T get(final Activity activity, final String platform) {
        Map<String, T> sub = mMap.get(activity);
        if (sub == null) {
            sub = new HashMap<>();
            mMap.put(activity, sub);
        }
        T api = sub.get(platform);
        if (api == null) {
            IFactory<T> factory = mFactories.get(platform);
            if (factory != null) {
                api = factory.create(activity);
                if (api != null) {
                    sub.put(platform, api);
                    activity.getApplication().registerActivityLifecycleCallbacks(new LifecycleCallback(activity, api));
                }
            }
        }
        return api;
    }

    public void onHandleResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (mMap.containsKey(activity)) {
            for (T api: mMap.get(activity).values()) {
                if (api != null) {
                    api.onResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private class LifecycleCallback implements Application.ActivityLifecycleCallbacks {

        final private Activity _activity;
        final private IResult _api;
        public LifecycleCallback(Activity activity, IResult api) {
            _activity = activity;
            _api = api;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity == _activity) {
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                if (_api instanceof IDisposable) {
                    ((IDisposable) _api).onDispose();
                }
            }

        }
    }

}