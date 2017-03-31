package ezy.sdk3rd.social.sdk;

import android.app.Activity;

/**
 * Created by ezy on 17/3/18.
 */

public class DefaultCallback<R> implements OnCallback<R> {
    OnCallback<R> onCallback;
    OnSucceed<R> onSuccess;

    public DefaultCallback(OnCallback<R> callback, OnSucceed<R> success) {
        onCallback = callback;
        onSuccess = success;
    }

    @Override
    public void onStarted(Activity activity) {
        if (onCallback != null) {
            onCallback.onStarted(activity);
        }
    }

    @Override
    public void onCompleted(Activity activity) {
        if (onCallback != null) {
            onCallback.onCompleted(activity);
        }
    }

    @Override
    public void onFailed(Activity activity, int code, String message) {
        if (onCallback != null) {
            onCallback.onFailed(activity, code, message);
        }
    }

    @Override
    public void onSucceed(Activity activity, R result) {
        if (onCallback != null) {
            onCallback.onSucceed(activity, result);
        }
        if (onSuccess != null) {
            onSuccess.onSucceed(result);
        }
    }
}
