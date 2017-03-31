package ezy.sdk3rd.social.share.image.resource;

import android.graphics.Bitmap;

import ezy.sdk3rd.social.share.image.ImageTool;

public class BytesResource implements ImageResource {
    public final byte[] bytes;

    public BytesResource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toUri() {
        return null;
    }

    @Override
    public Bitmap toBitmap() {
        return ImageTool.toBitmap(bytes);
    }

    @Override
    public byte[] toBytes() {
        return bytes;
    }
}