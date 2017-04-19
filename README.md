# sdk3rd [![](https://jitpack.io/v/czy1121/sdk3rd.svg)](https://jitpack.io/#czy1121/sdk3rd)

第三方SDK集成库，支持 授权/分享/支付 等功能

- 授权 目前支持 微信/QQ/新浪微博，客户端只需要配置APPID(新浪微博)
- 分享 目前支持 微信(会话/朋友圈/收藏)，QQ/QZone，新浪微博
- 支付 目前支持 支付宝/微信支付
- 对于授权与分享，客户端不用配置APPSECRET，只需要要APPID(新浪微博授权需要redirectUrl)
- 对于支付，APPID包含在由后端动态返回的paydata里
- 可注册自定义平台实现，满足特殊需求
- 分享并未实现ui，需要自己提供ui

![screenshot](https://raw.githubusercontent.com/czy1121/sdk3rd/master/screenshot.png)

目前这个库还不稳定，有些细节并未仔细考虑，有些功能也并未进行测试，支持的平台也不多，欢迎大家 star/issue/pr，共同完善这个库


### 关于授权

成功后的结果为 "code|{code}", "token|{openId}|{token}" 两种形式

- 微信授权是OAuth2.0返回的授权码(code)
- QQ授权是SSO返回的是token
- 微博授权其文档说是支持SSO与OAuth2.0，根据情况可能返回code与token

但客户端其实不需要关心是code还是token，直接把result发结服务端就好了


### 关于支付

很多支付类库会在客户端处理商品支付信息，其实这是不必要的，直接由服务端返回数据并交给支付SDK，然后处理支付结果就好了


### 关于分享

支持 纯文本/纯图片/图文/网页/音乐/视频 等分享类型

- 网页/音乐/视频 是卡片形式，可添加 标题(title)/描述(description)/缩略图(thumb)
- 微信朋友圈的网页链接不显示 description
- 新浪微博文本包含的网址会转成链接
- QZone 的 纯文本/纯图片/图文/视频 以“说说”的形式发布

|平台|纯文本|纯图片|图文|网页|音乐|视频|
|---|---|---|---|---|---|---|
|QQ|x|o|x|o|o|x|
|QZone|o|o|o|o|x|o|
|WXSession|o|o|x|o|o|o|
|WXTimeline|o|o|x|o|o|o|
|Weibo|o|o|o|x|x|x|
|SendToQQ|o|o|x|x|x|x|
|SendToWXS|o|o|x|x|x|x|
|SendToWXT|o|o|x|x|x|x|

## Demo

运行demo需要在`gradle.properties`中修改app的**包名，签名和为app申请的APPID**

```

APPLICATION_ID=ezy.demo.sdk3rd

APPID_QQ=YOUR_APPID
APPID_WEIXIN=YOUR_APPID
APPID_WEIBO=YOUR_APPID

SINGING_ALIAS=androiddebugkey
SINGING_PASSWORD=android
SINGING_FILE=debug.keystore
```

## 使用方法


### 添加依赖


``` groovy
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.czy1121.sdk3rd:sdk3rd:0.1.2'
    compile 'com.github.czy1121.sdk3rd:sdk3rd-alipay:0.1.1'
    compile 'com.github.czy1121.sdk3rd:sdk3rd-qq:0.1.0'

    compile 'com.sina.weibo.sdk:core:1.0.0:openDefaultRelease@aar'
    compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:1.0.2'

}
```

### 使用配置

配置APPID

``` java

PlatformConfig.useQQ(BuildConfig.APPID_QQ);
PlatformConfig.useWeixin(BuildConfig.APPID_WEIXIN);
PlatformConfig.useWeibo(BuildConfig.APPID_WEIBO, "http://www.sina.com/");
PlatformConfig.usePayments();

```


### 使用授权SDK

设置全局事件回调(非必要)


``` java
AuthorizeSDK.setDefaultCallback(new OnCallback() {
    @Override
    public void onStarted(Activity activity) {
        Log.e("ezy", "authorize started");
    }

    @Override
    public void onCompleted(Activity activity) {
        Log.e("ezy", "authorize completed");
    }

    @Override
    public void onSucceed(Activity activity, Object result) {
        Log.e("ezy", "authorize succeed");
    }

    @Override
    public void onFailed(Activity activity, int code, String message) {
        Log.e("ezy", "authorize failed [" + code + "]" + message);
    }
});

```

接收授权结果(新浪微博/QQ)

``` java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    AuthorizeSDK.onHandleResult(this, requestCode, resultCode, data);
}
```

调用授权

``` java
AuthorizeSDK.authorize(MainActivity.this, platform, new OnSucceed<String>() {
    @Override
    public void onSucceed(String result) {
        Toast.makeText(MainActivity.this, "登陆成功 - " + result, Toast.LENGTH_LONG).show();

    }
});
```

注册自定义平台，需要实现 IAuthorize 与相应的 IFactory

``` java
AuthorizeSDK.register(IFactory<IAuthorize> factory);
AuthorizeSDK.register(String platformName, String appId, Class<IAuthorize> clazz);
```


### 使用支付SDK

全局事件回调的设置与授权SDK类似


调用支付

``` java
PaymentSDK.pay(MainActivity.this, platform, paydata, new OnSucceed<String>() {
    @Override
    public void onSucceed(String result) {
        // todo
    }
});
```

注册自定义平台，需要实现 IPayable 与相应的 IFactory

``` java
PaymentSDK.register(IFactory<IPayable> factory);
PaymentSDK.register(String platformName, Class<IPayable> clazz);
```


### 使用分享SDK

全局事件回调的设置与授权SDK类似

...

分享结果的接收也与授权SDK类似(QQ)

...

注册自定义平台，需要实现 IShareable 与相应的 IFactory

``` java
ShareSDK.register(IFactory<IShareable> factory);
ShareSDK.register(String platformName, Class<IShareable> clazz);
```

分享


``` java
// 分享纯文本
ShareSDK.make(this, text).share(platform);
// 分享纯图片
ShareSDK.make(this, new MoImage(image)).share(platform);
// 分享图文
ShareSDK.make(this, text, new MoImage(image)).share(platform);

// 分享网页链接
ShareSDK.make(this, new MoWeb(url))
        .withTitle("这是标题")
        .withDescription("这是摘要")
        .withThumb(thumb)
        .share(platform, new OnSucceed<String>() {
           @Override
           public void onSucceed(String result) {
               Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_LONG).show();
           }
        });

// 分享音乐
ShareSDK.make(this, new MoMusic(url))
        .withTitle("这是标题")
        .withDescription("这是摘要")
        .withThumb(thumb)
        .share(platform);
```



## 参考

友盟+分享组件90%常见问题汇总
http://bbs.umeng.com/thread-17764-1-1.html

友盟各平台可分享内容预览
http://dev.umeng.com/social/android/share-detail#5

Android 不同平台分享内容的详细说明
http://wiki.mob.com/不同平台分享内容的详细说明

新浪微博SDK
https://github.com/sinaweibosdk/weibo_android_sdk


## License

```
Copyright 2017 czy1121

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
