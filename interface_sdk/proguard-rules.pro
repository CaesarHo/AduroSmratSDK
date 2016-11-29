# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-keep class org.eclipse.paho.clent.mqttv3.** {*;}
-keep class org.eclipse.paho.client.mqttv3.*$* { *; }
-keep class org.eclipse.paho.client.mqttv3.logging.JSR47Logger {
    *;
}
-optimizationpasses5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations
  !code/simplification/arithmetic,!field/*,!class/merging/*

//...begin
#混淆后的导出jar包的位置和jar包名
-outjars'E:\aduro_sdk.jar'

#原始jar包的位置和jar包名
-injars'E:\G:\AndroidAduroSmratSDK\interface_sdk\build\libs\sdk.jar'

#jar包依赖的其他库的位置和名称
-libraryjars'com.android.support:appcompat-v7:23.4.0'
-libraryjars'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.0.2'
-libraryjars'D:\Android\android-sdk-windows\platforms\android-23\android.jar'

#下面的Test类将不会被混淆，这样的类是需要被jar包使用者直接调用的
-keeppublic class  SerialHandler
 {
    public <fields>;
    public <methods>;
 }
//...end

-keeppublic class *extends android.app.Activity
-keeppublic class *extends android.app.Application
-keeppublic class *extends android.app.Service
-keeppublic class *extends android.content.BroadcastReceiver
-keeppublic class *extends android.content.ContentProvider
-keeppublic class *extends android.app.backup.BackupAgentHelper
-keeppublic class *extends android.preference.Preference
-keeppublic class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernamesclass *
 {
    native <methods>;
 }

-keepclasseswithmembersclass *
 {
    public <init>(android.content.Context,
 android.util.AttributeSet);
}

-keepclasseswithmembersclass *
 {
    public <init>(android.content.Context,
 android.util.AttributeSet, int);
}

-keepclassmembersclass *extends android.app.Activity
 {
   public void *(android.view.View);
}

-keepclassmembersenum *
 {
    public static **[]
 values();
    public static **
 valueOf(java.lang.String);
}

-keepclass *implements android.os.Serializable
 {
  public static final android.os.Parcelable$Creator
 *;
}

