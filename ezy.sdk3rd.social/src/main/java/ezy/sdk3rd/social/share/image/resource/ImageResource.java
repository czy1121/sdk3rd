package ezy.sdk3rd.social.share.image.resource;

import android.graphics.Bitmap;

public interface ImageResource {

    String toUri();

    Bitmap toBitmap();

    byte[] toBytes();
}