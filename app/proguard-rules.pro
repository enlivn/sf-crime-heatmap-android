-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes SourceFile, LineNumberTable

# Retrofit
-dontwarn retrofit.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

-keep class retrofit.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class com.android.example.sfcrimeheatmap.rest.models.** { *; }

# Butterknife
-dontwarn butterknife.internal.**

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}