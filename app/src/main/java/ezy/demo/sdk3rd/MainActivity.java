package ezy.demo.sdk3rd;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ezy.demo.sdk3rd.databinding.ActivityMainBinding;
import ezy.sdk3rd.social.AuthorizeSDK;
import ezy.sdk3rd.social.PaymentSDK;
import ezy.sdk3rd.social.PlatformConfig;
import ezy.sdk3rd.social.ShareSDK;
import ezy.sdk3rd.social.payment.PaymentVia;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.OnSucceed;
import ezy.sdk3rd.social.share.image.resource.FileResource;
import ezy.sdk3rd.social.share.image.resource.ResIdResource;
import ezy.sdk3rd.social.share.image.resource.UrlResource;
import ezy.sdk3rd.social.share.media.MoImage;
import ezy.sdk3rd.social.share.media.MoMusic;
import ezy.sdk3rd.social.share.media.MoVideo;
import ezy.sdk3rd.social.share.media.MoWeb;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        PlatformConfig.useQQ(BuildConfig.APPID_QQ);
        PlatformConfig.useWeixin(BuildConfig.APPID_WEIXIN);
        PlatformConfig.useWeibo(BuildConfig.APPID_WEIBO, "http://www.sina.com/");
        PlatformConfig.usePayments();

        ShareSDK.setDefaultCallback(new DefaultCallback("share"));
        PaymentSDK.setDefaultCallback(new DefaultCallback("payment"));
        AuthorizeSDK.setDefaultCallback(new DefaultCallback("auth"));


        binding.setOnShare(this);
        binding.setOnAuth(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platform = binding.spAuth.getSelectedItem().toString().toLowerCase();
                AuthorizeSDK.authorize(MainActivity.this, platform, new OnSucceed<String>() {
                    @Override
                    public void onSucceed(String result) {
                        Toast.makeText(MainActivity.this, "登陆成功 - " + result, Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });
        binding.setOnPay(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platform = "";
                String data = "";
                switch (v.getId()) {
                case R.id.btn_wxpay:
                    platform = PaymentVia.Wxpay;
                    data = binding.txtWxpaydata.getText().toString();
                    break;
                case R.id.btn_alipay:
                default:
                    platform = PaymentVia.Alipay;
                    data = binding.txtAlipaydata.getText().toString();
                    break;
                }
                PaymentSDK.pay(MainActivity.this, platform, data, new OnSucceed<String>() {
                    @Override
                    public void onSucceed(String result) {
                        Toast.makeText(MainActivity.this, "支付成功 - " + result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {

        FileResource file = new FileResource(new File("/storage/emulated/0/2017_03_23_16_36_07_receipt.jpg"));
        UrlResource imageUrl = new UrlResource("https://dn-mhke0kuv.qbox.me/e79942a9b8d8cdbb8dc3.jpg");
        ResIdResource resId = new ResIdResource(this, R.mipmap.ic_launcher, true);


        String platform = binding.spShare.getSelectedItem().toString().toLowerCase();
        OnSucceed onSucceed = new OnSucceed<String>() {
            @Override
            public void onSucceed(String result) {
                Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }
        };
        switch (binding.spType.getSelectedItemPosition()) {
        case 0: // web
            ShareSDK.make(this, new MoWeb("https://baidu.com")).withTitle("这是标题").withDescription("这是摘要").withThumb(resId).share(platform, onSucceed);
            break;
        case 1: // text
            ShareSDK.make(this, "这是文本～～～").share(platform, onSucceed);
            break;
        case 2: // image
            ShareSDK.make(this, new MoImage(file)).share(platform, onSucceed);
            break;
        case 3: // text+image
            ShareSDK.make(this, "这是文本～～～", new MoImage(file)).share(platform, onSucceed);
            break;
        case 4: // music
            String music = "https://archive.org/download/testmp3testfile/mpthreetest.mp3";
            ShareSDK.make(this, new MoMusic(music)).withTitle("这是标题").withDescription("这是摘要").withThumb(resId).share(platform, onSucceed);
            break;
        case 5: // video
            ShareSDK.make(this, new MoVideo("https://archive.org/download/Dokku_obrash/wmv.mp4"))
                    .withTitle("这是标题")
                    .withDescription("这是摘要")
                    .withThumb(resId)
                    .share(platform, onSucceed);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthorizeSDK.onHandleResult(this, requestCode, resultCode, data);
        ShareSDK.onHandleResult(this, requestCode, resultCode, data);
    }

    public static class DefaultCallback implements OnCallback {

        final String name;

        public DefaultCallback(String name) {
            this.name = name;
        }

        @Override
        public void onStarted(Activity activity) {
            Log.e("ezy", name + " started");
            Toast.makeText(activity, name + " started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCompleted(Activity activity) {
            Log.e("ezy", name + " completed");
            Toast.makeText(activity, name + " completed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSucceed(Activity activity, Object result) {
            Log.e("ezy", name + " succeed");
            Toast.makeText(activity, name + " succeed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed(Activity activity, int code, String message) {
            Log.e("ezy", name + " failed [" + code + "]" + message);
            Toast.makeText(activity, name + " failed [" + code + "]" + message, Toast.LENGTH_SHORT).show();
        }

    }
}
