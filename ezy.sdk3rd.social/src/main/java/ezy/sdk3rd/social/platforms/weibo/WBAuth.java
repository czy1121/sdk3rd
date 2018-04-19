package ezy.sdk3rd.social.platforms.weibo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import ezy.sdk3rd.social.authorize.IAuthorize;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

/**
 * Created by ezy on 17/3/18.
 */

public class WBAuth implements IAuthorize {
    public static final String TAG = "ezy.sdk3rd.weibo.auth";

    Activity mActivity;
    Platform mPlatform;

    SsoHandler mApi;

    WBAuth(Activity activity, Platform platform) {
        WbSdk.install(activity, new AuthInfo(activity, platform.getAppId(), platform.extra("redirectUrl"), "all"));
        mActivity = activity;
        mPlatform = platform;
        mApi = new SsoHandler(mActivity);

    }

    @Override
    public void authorize(@NonNull final OnCallback<String> callback) {
        callback.onStarted(mActivity);
        mApi.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken token) {
                Log.e(TAG, token.toString());
                if (token.isSessionValid()) {
                    callback.onSucceed(mActivity, "token|" + token.getUid() + "|" + token.getToken());
                }
                callback.onCompleted(mActivity);
            }

            @Override
            public void cancel() {
                Log.e(TAG, "用户取消了登录");
                callback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, "用户取消了登录");
            }

            @Override
            public void onFailure(WbConnectErrorMessage e) {
                Log.e(TAG, e.getErrorMessage());
                callback.onFailed(mActivity, ResultCode.RESULT_FAILED, e.getErrorMessage());
            }
        });
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (mApi != null) {
            mApi.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
