package ezy.sdk3rd.social.share.media;

import android.support.annotation.NonNull;

public class MoWeb implements IMediaObject {

    public String url;

    public MoWeb(@NonNull String url) {
        this.url = url;
    }

    @Override
    public int type() {
        return TYPE_WEB;
    }
}