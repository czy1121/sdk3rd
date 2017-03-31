


## manifest

``` xml
<application>
    <activity
        android:name="com.tencent.tauth.AuthActivity"
        android:launchMode="singleTask"
        android:noHistory="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW"/>

            <category android:name="android.intent.category.DEFAULT"/>
            <category android:name="android.intent.category.BROWSABLE"/>

            <data android:scheme="tencent${APP_ID_QQ}"/>
        </intent-filter>
    </activity>
</application>

```


## proguard

``` 
```