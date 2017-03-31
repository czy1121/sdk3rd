package ezy.sdk3rd.social.share.media;

public interface IMediaObject {
    int TYPE_INVALID = 0;
    int TYPE_TEXT = 1;
    int TYPE_IMAGE = 2;
    int TYPE_TEXT_IMAGE = 3;
    int TYPE_EMOJI = 4;
    int TYPE_MUSIC = 5;
    int TYPE_VIDEO = 6;
    int TYPE_WEB = 7;
    int TYPE_FILE = 8;

    int type();
}