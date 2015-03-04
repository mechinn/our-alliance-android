-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.billing.IInAppBillingService
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

# release
    -assumenosideeffects class android.util.Log {
        public static *** d(...);
        public static *** v(...);
    }

# Google Play Services
    -keep class * extends java.util.ListResourceBundle {
        protected Object[][] getContents();
    }

    -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
        public static final *** NULL;
    }

    -keepnames @com.google.android.gms.common.annotation.KeepName class *
    -keepclassmembernames class * {
        @com.google.android.gms.common.annotation.KeepName *;
    }

    -keepnames class * implements android.os.Parcelable {
        public static final ** CREATOR;
    }

# Squareup
    -dontwarn com.squareup.okhttp.**

# EventBus
    -keepclassmembers class ** {
        public void onEvent*(**);
    }

    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }

-keep class android.** { *; }
-keep interface android.** { *; }

-keep class com.google.** { *; }
-keep interface com.google.** { *; }

-keep class org.slf4j.** { *; }
-keep interface org.slf4j.** { *; }

-keep class org.apache.commons.** { *; }
-keep interface org.apache.commons.** { *; }

-keep class org.slf4j.** { *; }
-keep interface org.slf4j.** { *; }

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep class com.squareup.retrofit.** { *; }
-keep interface com.squareup.retrofit.** { *; }

-keep class com.squareup.picasso.** { *; }
-keep interface com.squareup.picasso.** { *; }

-keep class net.sf.supercsv.** { *; }
-keep interface net.sf.supercsv.** { *; }

-keep class com.crashlytics.android.** { *; }
-keep interface com.crashlytics.android.** { *; }

-keep class de.greenrobot.** { *; }
-keep interface de.greenrobot.** { *; }

-keep class org.lucasr.twowayview.** { *; }
-keep interface org.lucasr.twowayview.** { *; }

-keep class com.mobeta.android.dslv.** { *; }
-keep interface com.mobeta.android.dslv.** { *; }

-keep class com.jenzz.materialpreference.** { *; }
-keep interface com.jenzz.materialpreference.** { *; }

-keep class com.github.machinarius.preferencefragment.** { *; }
-keep interface com.github.machinarius.preferencefragment.** { *; }

-keep class com.michaelpardo.** { *; }
-keep interface com.michaelpardo.** { *; }

-keep class com.path.** { *; }
-keep interface com.path.** { *; }

-keep class com.github.mikephil.charting.** { *; }
-keep interface com.github.mikephil.charting.** { *; }

-keep class com.jakewharton.** { *; }
-keep interface com.jakewharton.** { *; }