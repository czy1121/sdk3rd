package ezy.sdk3rd.social.share;

import ezy.sdk3rd.social.sdk.IResult;
import ezy.sdk3rd.social.sdk.OnCallback;

/**
 * Created by ezy on 17/3/18.
 */

public interface IShareable extends IResult {
    void share(ShareData data, OnCallback<String> callback);
}
