package ezy.sdk3rd.social.platforms.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

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
        mActivity = activity;
        mPlatform = platform;
        mApi = new SsoHandler(mActivity, new AuthInfo(mActivity, platform.getAppId(), platform.extra("redirectUrl"), "all"));
    }

    @Override
    public void authorize(@NonNull final OnCallback<String> callback) {
        callback.onStarted(mActivity);
        mApi.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Log.e(TAG, bundle.toString());
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
                if (token.isSessionValid()) {
                    callback.onSucceed(mActivity, "token|" + token.getUid() + "|" + token.getToken());
                } else {
                    String code = bundle.getString("code");
                    callback.onSucceed(mActivity, "code|" + code);
                }
                callback.onCompleted(mActivity);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.e(TAG, e.getMessage());
                callback.onFailed(mActivity, ResultCode.RESULT_FAILED, e.getMessage());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "用户取消了登录");
                callback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, "用户取消了登录");
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
