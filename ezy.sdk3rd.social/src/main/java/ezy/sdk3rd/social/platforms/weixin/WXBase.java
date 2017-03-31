package ezy.sdk3rd.social.platforms.weixin;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.WeakHashMap;

import ezy.sdk3rd.social.sdk.IResult;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

/**
 * Created by ezy on 17/3/18.
 */

abstract class WXBase implements IResult, IWXAPIEventHandler {
    public static final String TAG = "ezy.sdk3rd.wx";

    public static int REQUEST_CODE = 1999;

    static WeakHashMap<IResult, Boolean> services = new WeakHashMap<>();

    final protected Activity mActivity;
    final protected Platform mPlatform;

    protected OnCallback mCallback;

    IWXAPI mApi;

    protected WXBase(Activity activity, Platform platform) {
        mActivity = activity;
        mPlatform = platform;
        if (!TextUtils.isEmpty(platform.getAppId())) {
            mApi = WXAPIFactory.createWXAPI(activity.getApplicationContext(), platform.getAppId(), true);
            mApi.registerApp(platform.getAppId());
        }
        services.put(this, true);
    }


    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (mApi != null && requestCode == REQUEST_CODE) {
            mApi.handleIntent(data, this);
        }
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e(TAG, "transaction = " + req.transaction + ", type = " + req.getType() + ", openId = " + req.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "transaction = " + resp.transaction + ", type = " + resp.getType() + ", errCode = " + resp.errCode + ", err = " + resp.errStr);

        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            switch (resp.getType()) {
            case ConstantsAPI.COMMAND_SENDAUTH:
                onResultOk((SendAuth.Resp) resp);
                break;
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                onResultOk((PayResp) resp);
                break;
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                onResultOk((SendMessageToWX.Resp) resp);
                break;
            }
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            mCallback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, "[" + resp.errCode + "]" + resp.errStr);
        } else {
            mCallback.onFailed(mActivity, ResultCode.RESULT_FAILED, "[" + resp.errCode + "]" + resp.errStr);
        }
        mCallback.onCompleted(mActivity);
    }

    protected void onResultOk(PayResp resp) {

    }

    protected void onResultOk(SendAuth.Resp resp) {
    }

    protected void onResultOk(SendMessageToWX.Resp resp) {
    }
}
