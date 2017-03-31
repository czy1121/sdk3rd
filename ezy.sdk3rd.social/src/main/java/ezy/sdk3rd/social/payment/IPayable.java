package ezy.sdk3rd.social.payment;

import ezy.sdk3rd.social.sdk.IResult;
import ezy.sdk3rd.social.sdk.OnCallback;

/**
 * Created by ezy on 17/3/18.
 */

public interface IPayable extends IResult {
    void pay(String data, OnCallback<String> callback);
}
