# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\AA\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
-dontwarn android.support.v4.**
-keep class android.support.v4.**{*;}

-dontwarn com.qdong.communal.library.**
-keep class com.qdong.communal.library.**{*;}

#-keep com.qdong.communal.library.widget.TabViews.PagerTab$* {
# public <interfaces>;
#public <fields>;
# public <methods>;
#}

#-keep com.qdong.communal.library.widget.TabViews.TabWithoutViewPager$* {
# public <interfaces>;
# public <fields>;
# public <methods>;
#}


-dontwarn Decoder.**
-keep class Decoder.**{*;}


-dontwarn java.io.**
-keep class java.io.**{*;}


-keep class  * implements java.io.Serializable{*;}
-keep class  java.lang.ref.SoftReference{*;}

#���˷���
-keepattributes Signature
#����ע��
-keepattributes *Annotation*
#����ö��
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

#���� Parcelable ��������
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}


