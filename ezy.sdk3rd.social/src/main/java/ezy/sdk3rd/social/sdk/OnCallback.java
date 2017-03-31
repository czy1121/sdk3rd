package ezy.sdk3rd.social.sdk;

import android.app.Activity;

/**
 * Created by ezy on 17/3/18.
 */

public interface OnCallback<T> {
    void onStarted(Activity activity);
    void onCompleted(Activity activity);
    void onSucceed(Activity activity, T result);
    void onFailed(Activity activity, int code, String message);
}
