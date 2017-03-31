package ezy.sdk3rd.social.share.media;

public class MoVideo implements IMediaObject {


    public String mediaUrl;
    public String mediaDataUrl;
    public String lowBandUrl;
    public String lowBandDataUrl;
    public int duration;

    public MoVideo(String url) {
        this.mediaUrl = url;
    }

    public MoVideo(String url, String dataUrl) {
        this.mediaUrl = url;
        this.mediaDataUrl = dataUrl;
    }
    public MoVideo withDuration(int value) {
        this.duration = value;
        return this;
    }
    public MoVideo withLowBand(String url) {
        this.lowBandUrl = url;
        return this;
    }
    public MoVideo withLowBand(String url, String dataUrl) {
        this.lowBandUrl = url;
        this.lowBandDataUrl = dataUrl;
        return this;
    }
    @Override
    public int type() {
        return TYPE_VIDEO;
    }
}