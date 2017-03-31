package ezy.sdk3rd.social.share.media;

import android.graphics.Bitmap;

import java.io.File;

import ezy.sdk3rd.social.share.image.resource.ImageResource;

public class MoEmoji extends MoImage {

    public MoEmoji(ImageResource source) {
        super(source);
    }

    public MoEmoji(File file) {
        super(file);
    }

    public MoEmoji(byte[] bytes) {
        super(bytes);
    }

    public MoEmoji(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public int type() {
        return TYPE_EMOJI;
    }
}