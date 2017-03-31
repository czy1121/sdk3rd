package ezy.sdk3rd.social.sdk;

import android.app.Activity;

/**
 * Created by ezy on 17/3/18.
 */

public interface IFactory<T extends IResult> {

    Platform getPlatform();

    T create(Activity activity);
}
