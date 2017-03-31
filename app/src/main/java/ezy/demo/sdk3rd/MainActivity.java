package ezy.demo.sdk3rd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ezy.sdk3rd.social.AuthorizeSDK;
import ezy.sdk3rd.social.PlatformConfig;
import ezy.sdk3rd.social.ShareSDK;
import ezy.sdk3rd.social.authorize.AuthorizeVia;
import ezy.sdk3rd.social.sdk.OnCallback;
import ezy.sdk3rd.social.sdk.OnSucceed;
import ezy.sdk3rd.social.share.ShareTo;
import ezy.sdk3rd.social.share.image.resource.FileResource;
import ezy.sdk3rd.social.share.image.resource.ResIdResource;
import ezy.sdk3rd.social.share.image.resource.UrlResource;
import ezy.sdk3rd.social.share.media.MoImage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlatformConfig.useQQ(BuildConfig.APPID_QQ);
        PlatformConfig.useWechat(BuildConfig.APPID_WECHAT);
        PlatformConfig.useWeibo(BuildConfig.APPID_WEIBO, "http://www.sina.com/");
        PlatformConfig.usePayments();

        ShareSDK.setDefaultCallback(new OnCallback() {
            @Override
            public void onStarted(Activity activity) {
                Log.e("ezy", "share started");
            }

            @Override
            public void onCompleted(Activity activity) {
                Log.e("ezy", "share completed");
            }

            @Override
            public void onSucceed(Activity activity, Object result) {
                Log.e("ezy", "share succeed");
            }

            @Override
            public void onFailed(Activity activity, int code, String message) {
                Log.e("ezy", "share failed [" + code + "]" + message);
            }
        });

        findViewById(R.id.btn_share_weibo).setOnClickListener(this);
        findViewById(R.id.btn_share_qq).setOnClickListener(this);
        findViewById(R.id.btn_share_qzone).setOnClickListener(this);
        findViewById(R.id.btn_share_wxsession).setOnClickListener(this);
        findViewById(R.id.btn_share_wxtimeline).setOnClickListener(this);
        findViewById(R.id.btn_share_wxfavorite).setOnClickListener(this);

        findViewById(R.id.btn_auth_weibo).setOnClickListener(mAuthListener);
        findViewById(R.id.btn_auth_qq).setOnClickListener(mAuthListener);
        findViewById(R.id.btn_auth_wechat).setOnClickListener(mAuthListener);

    }

    View.OnClickListener mAuthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String platform = AuthorizeVia.QQ;
            switch (v.getId()) {
            case R.id.btn_auth_qq:
                platform = AuthorizeVia.QQ;
                break;
            case R.id.btn_auth_wechat:
                platform = AuthorizeVia.Wechat;
                break;
            case R.id.btn_auth_weibo:
                platform = AuthorizeVia.Weibo;
                break;
            }
            AuthorizeSDK.authorize(MainActivity.this, platform, new OnSucceed<String>() {
                @Override
                public void onSucceed(String result) {
                    Toast.makeText(MainActivity.this, "登陆成功 - " + result, Toast.LENGTH_LONG).show();

                }
            });
        }

    };
    String paydata = "service=\"mobile.securitypay.pay\"&partner=\"2088011288252216\"&notify_url=\"https://paygateway.mogujie" +
            ".com/frontend/callback/alipay/payNotify\"&_input_charset=\"utf-8\"&out_trade_no=\"1720B154JIGT3398\"&subject=\"美丽说-美丽优选\"&payment_type" +
            "=\"1\"&seller_id=\"meilishuodt@126.com\"&total_fee=\"60.00\"&body=\"170001\"&goods_type=\"0\"&it_b_pay=\"60m\"";

    String wxpwydata = "{\"timestamp\":\"1490721808\",\"sign\":\"E9F094C2E231835B6835E01EA2D2C569\"," +
            "\"noncestr\":\"xisx41rn610wn8b1efl8tcknw926wht7\",\"partnerid\":\"10014650\",\"prepayid\":\"wx201703290123280d389e06550430073958\"," +
            "\"package\":\"Sign=WXPay\",\"appid\":\"wx61ef83a134a439c4\"}";
    @Override
    public void onClick(View v) {

        String music = "https://nj01ct01.baidupcs.com/file/fe2803a864c92e53cf6f5507b2869ccc?bkt=p3-1400fe2803a864c92e53cf6f5507b2869ccc853a56e00000002fc33b&fid=4250099281-250528-767932956710912&time=1490860986&sign=FDTAXGERLBHS-DCb740ccc5511e5e8fedcff06b081203-vfYL0mDRDTJvTtlYcqoBxSeaHv4%3D&to=63&size=3130171&sta_dx=3130171&sta_cs=0&sta_ft=mp3&sta_ct=7&sta_mt=7&fm2=MH,Yangquan,Netizen-anywhere,,guangdongct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400fe2803a864c92e53cf6f5507b2869ccc853a56e00000002fc33b&sl=75563087&expires=8h&rt=sh&r=362687042&mlogid=2056480365358258226&vuk=1812840510&vbdid=4143526863&fin=Last+Call.mp3&fn=Last+Call.mp3&rtype=1&iv=0&dp-logid=2056480365358258226&dp-callid=0.1.1&hps=1&csl=350&csign=LVWW2SkfKEw6xq57BRW0Z%2FBSOoc%3D&by=themis";
        String text = "这是文本哈~~";
        String image = "https://dn-mhke0kuv.qbox.me/e79942a9b8d8cdbb8dc3.jpg";
        String url = "https://baidu.com";
        String platform = ShareTo.QQ;
        switch (v.getId()) {
        case R.id.btn_share_qzone:
            platform = ShareTo.QZone;
            break;
        case R.id.btn_share_qq:
            platform = ShareTo.QQ;
            break;
        case R.id.btn_share_wxsession:
            platform = ShareTo.WXSession;
            break;
        case R.id.btn_share_wxtimeline:
            platform = ShareTo.WXTimeline;
            break;
        case R.id.btn_share_wxfavorite:
            platform = ShareTo.WXFavorite;
            break;
        case R.id.btn_share_weibo:
            platform = ShareTo.Weibo;
            break;
        }
        FileResource file = new FileResource(new File("/storage/emulated/0/2017_03_23_16_36_07_receipt.jpg"));
        UrlResource imageUrl = new UrlResource(image);
        ResIdResource resId = new ResIdResource(this, R.mipmap.ic_launcher, true);
        ShareSDK.make(this, new MoImage(imageUrl))
                .withUrl(url)
                .withTitle("这是标题")
                .withDescription("这是摘要")
                .withThumb(resId)
                .share(platform, new OnSucceed<String>() {
                    @Override
                    public void onSucceed(String result) {
                        Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthorizeSDK.onHandleResult(this, requestCode, resultCode, data);
        ShareSDK.onHandleResult(this, requestCode, resultCode, data);
    }
}
