package ezy.sdk3rd.social;

import android.app.Activity;
import android.content.Intent;

import ezy.sdk3rd.social.sdk.DefaultCallback;
import ezy.sdk3rd.social.sdk.IFactory;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.OnSucceed;
import ezy.sdk3rd.social.sdk.ResultCode;
import ezy.sdk3rd.social.sdk.Sdk;
import ezy.sdk3rd.social.share.IShareable;
import ezy.sdk3rd.social.share.ShareData;
import ezy.sdk3rd.social.share.image.resource.ImageResource;
import ezy.sdk3rd.social.share.media.IMediaObject;
import ezy.sdk3rd.social.share.media.MoImage;

public class ShareSDK {
    static Sdk<IShareable> sdk = new Sdk<>();

    public static void setDefaultCallback(OnCallback callback) {
        sdk.setDefaultCallback(callback);
    }

    public static <T extends IShareable> void register(String name, String appId, Class<T> clazz) {
        sdk.register(name, appId, clazz);
    }

    public static <T extends IShareable>  void register(IFactory<T> factory) {
        sdk.register(factory);
    }


    private ShareData mData = new ShareData();

    private Activity mActivity;

    private ShareSDK(Activity activity, String text, IMediaObject media) {
        mActivity = activity;
        mData.text = text;
        mData.media = media;
    }

    // 文本
    public static ShareSDK make(Activity activity, String text) {
        return new ShareSDK(activity, text, null);
    }

    // 图片
    public static ShareSDK make(Activity activity, ImageResource image) {
        return new ShareSDK(activity, null, new MoImage(image));
    }

    // 图片、音乐、视频、文件
    public static ShareSDK make(Activity activity, IMediaObject media) {
        return new ShareSDK(activity, null, media);
    }

    public static ShareSDK make(Activity activity, String text, IMediaObject media) {
        return new ShareSDK(activity, text, media);
    }

    public ShareSDK withUrl(String value) {
        mData.url = value;
        return this;
    }

    public ShareSDK withTitle(String value) {
        mData.title = value;
        return this;
    }

    public ShareSDK withDescription(String value) {
        mData.description = value;
        return this;
    }

    public ShareSDK withThumb(ImageResource thumb) {
        mData.thumb = thumb;
        return this;
    }

    public void share(String platform) {
        share(platform, new DefaultCallback(sdk.getDefaultCallback(), null));
    }

    public void share(String platform, OnSucceed<String> listener) {
        share(platform, new DefaultCallback(sdk.getDefaultCallback(), listener));
    }
    public void share(String platform, OnCallback<String> callback) {
        if (!sdk.isSupport(platform)) {
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "不支持的平台[" + platform + "]");
            return;
        }
        IShareable api = sdk.get(mActivity, platform);
        if (api == null) {
            return;
        }
        api.share(mData, callback);
    }

    public static void onHandleResult(Activity activity, int requestCode, int resultCode, Intent data) {
        sdk.onHandleResult(activity, requestCode, resultCode, data);
    }
}