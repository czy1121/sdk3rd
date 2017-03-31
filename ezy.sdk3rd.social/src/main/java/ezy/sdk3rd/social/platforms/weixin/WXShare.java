package ezy.sdk3rd.social.platforms.weixin;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;
import ezy.sdk3rd.social.share.IShareable;
import ezy.sdk3rd.social.share.ShareData;
import ezy.sdk3rd.social.share.ShareTo;
import ezy.sdk3rd.social.share.media.IMediaObject;
import ezy.sdk3rd.social.share.media.MoImage;
import ezy.sdk3rd.social.share.media.MoMusic;
import ezy.sdk3rd.social.share.media.MoVideo;
import ezy.sdk3rd.social.share.media.MoWeb;

/**
 * Created by ezy on 17/3/18.
 */

public class WXShare extends WXBase implements IShareable {

    WXShare(Activity activity, Platform platform) {
        super(activity, platform);
    }

    @Override
    public void share(@NonNull final ShareData data, @NonNull final OnCallback<String> callback) {

        if (!mApi.isWXAppInstalled()) {
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "您未安装微信!");
            return;
        }
        mCallback = callback;
        new AsyncTask<ShareData, Void, Void>() {
            @Override
            protected Void doInBackground(ShareData... params) {
                doShare(params[0]);
                return null;
            }
        }.execute(data);
    }

    void doShare(ShareData data) {

        WXMediaMessage message = new WXMediaMessage();
        if (data.hasTitle()) {
            message.title = data.title;
        }
        if (data.hasDescription()) {
            message.description = data.description;
        }
        if (data.hasThumb()) {
            message.thumbData = data.thumb.toBytes();
        }

        switch (data.type()) {
        case IMediaObject.TYPE_TEXT:
            message.mediaObject = new WXTextObject(data.text);
            break;
        case IMediaObject.TYPE_WEB:
            message.mediaObject = new WXWebpageObject(((MoWeb) data.media).url);
            break;
        case IMediaObject.TYPE_EMOJI:
        case IMediaObject.TYPE_IMAGE: {
            MoImage mo = (MoImage) data.media;
            WXImageObject wxo = new WXImageObject();
            wxo.imagePath = mo.toUri();
            if (TextUtils.isEmpty(wxo.imagePath)) {
                wxo.imageData = mo.toBytes();
            }
            message.mediaObject = wxo;
        }
        break;
        case IMediaObject.TYPE_VIDEO: {
            MoVideo mo = (MoVideo) data.media;
            WXVideoObject wxo = new WXVideoObject();
            wxo.videoUrl = mo.mediaUrl;
            wxo.videoLowBandUrl = mo.lowBandUrl;
            message.mediaObject = wxo;
        }
        break;
        case IMediaObject.TYPE_MUSIC: {
            MoMusic mo = (MoMusic) data.media;
            WXMusicObject wxo = new WXMusicObject();
            wxo.musicUrl = mo.mediaUrl;
            wxo.musicDataUrl = mo.mediaDataUrl;
            wxo.musicLowBandUrl = mo.lowBandUrl;
            wxo.musicLowBandDataUrl = mo.lowBandDataUrl;
            message.mediaObject = wxo;
        }
        break;

        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = toScene(mPlatform.getName());

        mCallback.onStarted(mActivity);
        mApi.sendReq(req);
    }

    @Override
    protected void onResultOk(SendMessageToWX.Resp resp) {
        mCallback.onSucceed(mActivity, "");
    }

    int toScene(String platform) {
        switch (platform) {
        case ShareTo.WXSession:
            return 0;
        case ShareTo.WXTimeline:
            return 1;
        case ShareTo.WXFavorite:
            return 2;
        }
        return 0;
    }


}
