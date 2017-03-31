package ezy.sdk3rd.social.platforms.wechat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendAuth;

import ezy.sdk3rd.social.authorize.IAuthorize;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

/**
 * Created by ezy on 17/3/18.
 */

public class WXAuth extends WXBase implements IAuthorize {

    WXAuth(Activity activity, Platform platform) {
        super(activity, platform);
    }


    @Override
    public void authorize(@NonNull OnCallback<String> callback) {
        mCallback = callback;
        if (!mApi.isWXAppInstalled()) {
            mCallback.onFailed(mActivity, ResultCode.RESULT_FAILED, "您未安装微信!");
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat";

        Log.e(TAG, "send start");
        mCallback.onStarted(mActivity);
        boolean ret = mApi.sendReq(req);
        Log.e(TAG, "send end " + ret);
    }

    @Override
    protected void onResultOk(SendAuth.Resp resp) {
        Log.e(TAG, "code = " + resp.code);
        mCallback.onSucceed(mActivity, "code|" + resp.code);
    }

}
