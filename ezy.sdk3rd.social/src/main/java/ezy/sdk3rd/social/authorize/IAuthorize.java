package ezy.sdk3rd.social.authorize;

import ezy.sdk3rd.social.sdk.IResult;
import ezy.sdk3rd.social.sdk.OnCallback;

/**
 * Created by ezy on 17/3/18.
 */

public interface IAuthorize extends IResult {
    void authorize(OnCallback<String> callback);
}
