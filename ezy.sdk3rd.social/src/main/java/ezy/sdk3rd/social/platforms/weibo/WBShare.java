package ezy.sdk3rd.social.platforms.weibo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import java.util.Map;
import java.util.WeakHashMap;

import ezy.sdk3rd.social.sdk.IResult;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;
import ezy.sdk3rd.social.share.IShareable;
import ezy.sdk3rd.social.share.ShareData;
import ezy.sdk3rd.social.share.image.resource.ImageResource;
import ezy.sdk3rd.social.share.media.MoImage;

/**
 * Created by ezy on 17/3/18.
 */

public class WBShare implements IShareable, IWeiboHandler.Response {
    public static final String TAG = "ezy.sdk3rd.weibo.share";

    public static int REQUEST_CODE = 1998;

    static Map<IResult, Boolean> services = new WeakHashMap<>();


    Activity mActivity;
    Platform mPlatform;

    IWeiboShareAPI mApi;
    OnCallback<String> mCallback;

    WBShare(Activity activity, Platform platform) {
        mActivity = activity;
        mPlatform = platform;
        mApi = WeiboShareSDK.createWeiboAPI(mActivity, platform.getAppId());
        mApi.registerApp();
        services.put(this, true);
    }

    TextObject toText(String text) {
        TextObject object = new TextObject();
        object.text = text;
        return object;
    }

    ImageObject toImage(ImageResource resource) {
        ImageObject object = new ImageObject();
        object.imagePath = resource.toUri();
        if (TextUtils.isEmpty(object.imagePath)) {
            object.imageData = resource.toBytes();
        }
        return object;
    }

    @Override
    public void share(@NonNull final ShareData data, @NonNull final OnCallback<String> callback) {

        if (!mApi.isWeiboAppInstalled()) {
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "您未安装微博!");
            return;
        }

        WeiboMultiMessage message = new WeiboMultiMessage();

        if (data.hasText()) {
            message.textObject = toText(data.hasUrl() ? (data.text + " " + data.url) : data.text);
        }

        if (data.media instanceof MoImage) {
            message.imageObject = toImage(((MoImage) data.media).resource);
        }

        if (message.textObject == null && message.imageObject == null) {
            // unsupported
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "不支持的分享类型");
            return;
        }

        mCallback = callback;
        mCallback.onStarted(mActivity);

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = message;
        request.packageName = mActivity.getPackageName();

        mApi.sendRequest(mActivity, request);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "==> requestCode = " + requestCode + ", " + data);
        if (mApi != null && requestCode == REQUEST_CODE) {
            mApi.handleWeiboResponse(data, this);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response == null) {
            switch (response.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                mCallback.onSucceed(mActivity, "");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                mCallback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, response.errMsg);
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                mCallback.onFailed(mActivity, ResultCode.RESULT_FAILED, response.errMsg);
                break;
            }
            mCallback.onCompleted(mActivity);
        }
    }
}
