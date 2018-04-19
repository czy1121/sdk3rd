package ezy.sdk3rd.social.platforms.weibo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

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

public class WBShare implements IShareable, WbShareCallback {
    public static final String TAG = "ezy.sdk3rd.weibo.share";

    public static int REQUEST_CODE = 1998;

    static Map<IResult, Boolean> services = new WeakHashMap<>();


    Activity mActivity;
    Platform mPlatform;

    WbShareHandler mApi;
    OnCallback<String> mCallback;

    WBShare(Activity activity, Platform platform) {
        WbSdk.install(activity, new AuthInfo(activity, platform.getAppId(), platform.extra("redirectUrl"), "all"));
        mActivity = activity;
        mPlatform = platform;
        mApi = new WbShareHandler(mActivity);
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

    WebpageObject toWeb(ShareData data) {
        WebpageObject mo = new WebpageObject();
        mo.identify = Utility.generateGUID();
        mo.title = data.title;
        mo.description = data.description;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mo.setThumbImage(data.thumb.toBitmap());
        mo.actionUrl = data.url;
        mo.defaultText = "Webpage 默认文案";
        return mo;
    }

    @Override
    public void share(@NonNull final ShareData data, @NonNull final OnCallback<String> callback) {

        //        if (!mApi.isWeiboAppInstalled()) {
        //            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "您未安装微博!");
        //            return;
        //        }

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

        mApi.shareMessage(message, true);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "==> requestCode = " + requestCode + ", " + data);
        if (mApi != null && requestCode == REQUEST_CODE) {
            mApi.doResultIntent(data, this);
        }
    }

    @Override
    public void onWbShareCancel() {
        mCallback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, "");
        mCallback.onCompleted(mActivity);
    }

    @Override
    public void onWbShareFail() {
        mCallback.onFailed(mActivity, ResultCode.RESULT_FAILED, "");
        mCallback.onCompleted(mActivity);
    }

    @Override
    public void onWbShareSuccess() {
        mCallback.onSucceed(mActivity, "");
        mCallback.onCompleted(mActivity);
    }

}
