package ezy.sdk3rd.social.share.image.resource;

import android.graphics.Bitmap;

import ezy.sdk3rd.social.share.image.ImageTool;

public class UrlResource implements ImageResource {
    public final String url;

    public UrlResource(String url) {
        this.url = url;
    }

    @Override
    public String toUri() {
        return url;
    }

    @Override
    public Bitmap toBitmap() {
        return ImageTool.toBitmap(ImageTool.loadNetImage(url));
    }

    @Override
    public byte[] toBytes() {
        return ImageTool.loadNetImage(url);
    }
}