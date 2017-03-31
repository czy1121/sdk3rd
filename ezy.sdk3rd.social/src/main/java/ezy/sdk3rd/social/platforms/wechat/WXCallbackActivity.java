package ezy.sdk3rd.social.platforms.wechat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ezy.sdk3rd.social.sdk.IResult;

public class WXCallbackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e(WXBase.TAG, "==> " + ((getIntent() == null) ? "" : getIntent().getExtras()));

        for (IResult service: WXBase.services.keySet()) {
            if (service != null) {
                service.onResult(WXBase.REQUEST_CODE, Activity.RESULT_OK, intent);
            }
        }
        finish();
    }
}