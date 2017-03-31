package ezy.sdk3rd.social;

import android.app.Activity;
import android.content.Intent;

import ezy.sdk3rd.social.payment.IPayable;
import ezy.sdk3rd.social.sdk.DefaultCallback;
import ezy.sdk3rd.social.sdk.IFactory;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.OnSucceed;
import ezy.sdk3rd.social.sdk.ResultCode;
import ezy.sdk3rd.social.sdk.Sdk;

public class PaymentSDK {
    static Sdk<IPayable> sdk = new Sdk<>();

    public static void setDefaultCallback(OnCallback callback) {
        sdk.setDefaultCallback(callback);
    }

    public static <T extends IPayable> void register(String name, Class<T> clazz) {
        sdk.register(name, "", clazz);
    }

    public static <T extends IPayable>  void register(IFactory<T> factory) {
        sdk.register(factory);
    }

    public static void pay(Activity activity, String platform, String data, OnSucceed<String> listener) {
        pay(activity, platform, data, new DefaultCallback(sdk.getDefaultCallback(), listener));
    }

    public static void pay(Activity activity, String platform, String data, OnCallback<String> callback) {
        if (!sdk.isSupport(platform)) {
            callback.onFailed(activity, ResultCode.RESULT_FAILED, "不支持的平台[" + platform + "]");
            return;
        }
        IPayable api = sdk.get(activity, platform);
        if (api == null) {
            return;
        }
        api.pay(data, callback);
    }

    public static void onHandleResult(Activity activity, int requestCode, int resultCode, Intent data) {
        sdk.onHandleResult(activity, requestCode, resultCode, data);
    }
}