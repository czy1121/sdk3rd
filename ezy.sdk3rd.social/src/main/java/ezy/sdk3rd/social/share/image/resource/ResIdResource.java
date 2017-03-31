package ezy.sdk3rd.social.share.image.resource;

import android.content.Context;
import android.graphics.Bitmap;

import ezy.sdk3rd.social.share.image.ImageTool;

public class ResIdResource implements ImageResource {
    Context mContext;
    int mResId;
    boolean mIsRaw;

    public ResIdResource(Context context, int resId, boolean isRaw) {
        mContext = context;
        mResId = resId;
        mIsRaw = isRaw;
    }

    @Override
    public String toUri() {
        return null;
    }

    @Override
    public Bitmap toBitmap() {
        return ImageTool.toBitmap(mContext, mResId, mIsRaw);
    }

    @Override
    public byte[] toBytes() {
        return ImageTool.toBytes(mContext, mResId, mIsRaw, Bitmap.CompressFormat.JPEG);
    }
}