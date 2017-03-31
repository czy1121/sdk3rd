package ezy.sdk3rd.social.platforms.qq;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import ezy.sdk3rd.social.authorize.IAuthorize;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

/**
 * Created by ezy on 17/3/18.
 */

public class QQAuth implements IAuthorize {
    public static final String TAG = "ezy.sdk3rd.qq.auth";

    Activity mActivity;
    Platform mPlatform;
    Tencent mApi;

    IUiListener mListener;

    QQAuth(Activity activity, Platform platform) {
        mActivity = activity;
        mPlatform = platform;
        mApi = Tencent.createInstance(platform.getAppId(), mActivity);
    }

    String toMessage(UiError error) {
        return "[" + error.errorCode + "]" + error.errorMessage;
    }

    @Override
    public void authorize(@NonNull final OnCallback<String> callback) {
        mListener = new IUiListener() {
            @Override
            public void onComplete(Object response) {
                Log.e(TAG, "complete ==> " + response);
                if (response instanceof JSONObject && ((JSONObject) response).length() > 0) {
                    JSONObject jo = (JSONObject) response;
                    String token = jo.optString(Constants.PARAM_ACCESS_TOKEN);
                    String expires = jo.optString(Constants.PARAM_EXPIRES_IN);
                    String openId = jo.optString(Constants.PARAM_OPEN_ID);
                    if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                        mApi.setAccessToken(token, expires);
                        mApi.setOpenId(openId);
                    }
                    callback.onSucceed(mActivity, "token|" + mApi.getOpenId() + "|" + mApi.getAccessToken());
                } else {
                    callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "登录失败: 返回为空");
                }
                callback.onCompleted(mActivity);
            }


            @Override
            public void onError(UiError e) {
                Log.e(TAG, "error ==> " + e.errorDetail);
                callback.onFailed(mActivity, ResultCode.RESULT_FAILED, toMessage(e));
            }

            @Override
            public void onCancel() {
                callback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, "用户取消了登录");
            }
        };
        callback.onStarted(mActivity);
        mApi.login(mActivity, "get_simple_userinfo", mListener);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
        }
    }

}
