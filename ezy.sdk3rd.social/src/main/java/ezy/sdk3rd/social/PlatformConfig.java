package ezy.sdk3rd.social;

import android.support.annotation.NonNull;

import ezy.sdk3rd.social.authorize.AuthorizeVia;
import ezy.sdk3rd.social.payment.PaymentVia;
import ezy.sdk3rd.social.platforms.alipay.Alipay;
import ezy.sdk3rd.social.platforms.qq.QQAuth;
import ezy.sdk3rd.social.platforms.qq.TXShare;
import ezy.sdk3rd.social.platforms.send.SendShare;
import ezy.sdk3rd.social.platforms.weixin.WXAuth;
import ezy.sdk3rd.social.platforms.weixin.WXPayment;
import ezy.sdk3rd.social.platforms.weixin.WXShare;
import ezy.sdk3rd.social.platforms.weibo.WBAuth;
import ezy.sdk3rd.social.platforms.weibo.WBShare;
import ezy.sdk3rd.social.sdk.DefaultFactory;
import ezy.sdk3rd.social.sdk.Platform;
import ezy.sdk3rd.social.share.ShareTo;

/**
 * Created by ezy on 17/3/18.
 */

public class PlatformConfig {

    public static void useWeibo(@NonNull String appId, @NonNull String redirectUrl) {
        Platform platform = new Platform(AuthorizeVia.Weibo, appId).extra("redirectUrl", redirectUrl);
        AuthorizeSDK.register(new DefaultFactory(platform, WBAuth.class));
        ShareSDK.register(new DefaultFactory(platform, WBShare.class));
    }

    public static void useQQ(@NonNull String appId) {
        AuthorizeSDK.register(AuthorizeVia.QQ, appId, QQAuth.class);

        ShareSDK.register(ShareTo.QQ, appId, TXShare.class);
        ShareSDK.register(ShareTo.QZone, appId, TXShare.class);
        ShareSDK.register(ShareTo.ToQQ, "", SendShare.class);
    }

    public static void useWeixin(@NonNull String appId) {
        AuthorizeSDK.register(AuthorizeVia.Weixin, appId, WXAuth.class);

        ShareSDK.register(ShareTo.WXSession, appId, WXShare.class);
        ShareSDK.register(ShareTo.WXTimeline, appId, WXShare.class);
        ShareSDK.register(ShareTo.WXFavorite, appId, WXShare.class);
        ShareSDK.register(ShareTo.ToWXSession, "", SendShare.class);
        ShareSDK.register(ShareTo.ToWXTimeline, "", SendShare.class);
    }
    public static void usePayments() {
        PaymentSDK.register(PaymentVia.Wxpay, WXPayment.class);
        PaymentSDK.register(PaymentVia.Alipay, Alipay.class);
    }
}
