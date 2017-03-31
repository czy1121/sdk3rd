package ezy.sdk3rd.social.platforms.alipay;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;

import ezy.sdk3rd.social.payment.IPayable;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.sdk.ResultCode;

public class Alipay implements IPayable {
    public static final String TAG = "ezy.sdk3rd.alipay";

    Activity mActivity;

    Alipay(Activity activity, Platform platform) {
        mActivity = activity;
    }

    @Override
    public void pay(final String data, final OnCallback<String> callback) {

        new AsyncTask<String, Void, Result>() {
            @Override
            protected void onPreExecute() {
                callback.onStarted(mActivity);
            }

            @Override
            protected Result doInBackground(String... params) {
                final PayTask task = new PayTask(mActivity);
                return new Result(task.pay(data, true));
            }

            @Override
            protected void onPostExecute(Result result) {
                String message = "[" + result.resultStatus + "]" + result.resultText;
                if (result.isSuccess()) {
                    callback.onSucceed(mActivity, "");
                } else if (result.isPending()) {
                    callback.onFailed(mActivity, ResultCode.RESULT_PENDING, message);
                } else if (result.isCancelled()){
                    callback.onFailed(mActivity, ResultCode.RESULT_CANCELLED, message);
                } else {
                    callback.onFailed(mActivity, ResultCode.RESULT_FAILED, message);
                }
                callback.onCompleted(mActivity);
            }
        }.execute(data);

    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {

    }
}