package ezy.sdk3rd.social.platforms.wechat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import ezy.sdk3rd.social.payment.IPayable;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

/**
 * Created by ezy on 17/3/18.
 */

public class WXPayment extends WXBase implements IPayable {


    WXPayment(Activity activity, Platform platform) {
        super(activity, platform);
    }

    @Override
    public void pay(String data, final @NonNull OnCallback<String> callback) {
        PayReq req = new PayReq();
        Log.e(TAG, "data ==> " + data);
        try {
            JSONObject o = new JSONObject(data);
            req.appId = o.getString("appid");
            req.partnerId = o.getString("partnerid");
            req.prepayId = o.getString("prepayid");
            req.packageValue = o.getString("package");
            req.nonceStr = o.getString("noncestr");
            req.timeStamp = o.getString("timestamp");
            req.sign = o.getString("sign");
            req.transaction = req.nonceStr;
        } catch (Exception e) {
            Log.e(TAG, "parse error ==> " + e.toString());
        }
        if (!TextUtils.isEmpty(req.appId)) {
            mApi = WXAPIFactory.createWXAPI(mActivity.getApplicationContext(), req.appId, true);
            mApi.registerApp(req.appId);
        }
        mCallback = callback;
        if (!mApi.isWXAppInstalled()) {
            mCallback.onFailed(mActivity, ResultCode.RESULT_FAILED, "您未安装微信!");
            return;
        }
        mCallback.onStarted(mActivity);
        boolean ret = mApi.sendReq(req);
        Log.e(TAG, "send end, pay request  ==> " + ret);

        if (ret) {
        }
    }

    @Override
    protected void onResultOk(PayResp resp) {
        Log.e(TAG, "prepayId = " + resp.prepayId);
        mCallback.onSucceed(mActivity, resp.prepayId);
    }
}
