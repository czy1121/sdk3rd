package ezy.sdk3rd.social.platforms.send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.util.List;

import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;
import ezy.sdk3rd.social.share.IShareable;
import ezy.sdk3rd.social.share.ShareData;
import ezy.sdk3rd.social.share.ShareTo;
import ezy.sdk3rd.social.share.image.resource.BitmapResource;
import ezy.sdk3rd.social.share.image.resource.FileResource;
import ezy.sdk3rd.social.share.image.resource.ImageResource;
import ezy.sdk3rd.social.share.media.IMediaObject;
import ezy.sdk3rd.social.share.media.MoImage;

/**
 * Created by ezy on 17/3/18.
 */

public class SendShare implements IShareable {
    public static final String TAG = "ezy.sdk3rd.qq.share";

    static final String PACKAGE_QQ = "com.tencent.mobileqq";
    static final String PACKAGE_WX = "com.tencent.mm";

    Activity mActivity;
    Platform mPlatform;


    SendShare(Activity activity, Platform platform) {
        mActivity = activity;
        mPlatform = platform;
    }

    @Override
    public void share(@NonNull final ShareData data, @NonNull final OnCallback<String> callback) {

        if (mPlatform.getName().equals(ShareTo.ToQQ)) {
            if (!isApplicationInstalled(mActivity, PACKAGE_QQ)) {
                callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "无法分享，请先安装QQ");
                return;
            }
        } else {
            if (!isApplicationInstalled(mActivity, PACKAGE_WX)) {
                callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "无法分享，请先安装微信");
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (mPlatform.getName().equals(ShareTo.ToQQ)) {
            intent.setClassName(PACKAGE_QQ, "com.tencent.mobileqq.activity.JumpActivity");
        } else if (mPlatform.getName().equals(ShareTo.ToWXSession)) {
            intent.setClassName(PACKAGE_WX, "com.tencent.mm.ui.tools.ShareImgUI");
        } else {
            intent.setClassName(PACKAGE_WX, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        }

        switch (data.type()) {
        case IMediaObject.TYPE_IMAGE:
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            ImageResource resource = ((MoImage) data.media).resource;
            if (resource instanceof BitmapResource) {
                final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), resource.toBitmap(), null, null));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            } else if (resource instanceof FileResource) {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(((FileResource) resource).file));
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(resource.toUri()));
            }
            break;
        case IMediaObject.TYPE_TEXT:
            intent.setType("text/plain");
            intent.putExtra("Kdescription", data.text);
            intent.putExtra(Intent.EXTRA_TEXT, data.text);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            break;
        default:
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "不支持的分享类型");
            return;
        }
        if (isIntentAvailable(mActivity, intent)) {
            mActivity.startActivity(intent);
        } else {
            callback.onFailed(mActivity, ResultCode.RESULT_FAILED, "分享失败");
            return;
        }
    }

    boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    boolean isApplicationInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
    }
}
