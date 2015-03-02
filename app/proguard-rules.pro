# Two Way View
    -keep class org.lucasr.twowayview.** { *; }

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
        (java.lang.Throwable);
    }

# Butter Knife
    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewInjector { *; }

    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }

    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }