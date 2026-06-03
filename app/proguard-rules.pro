# Add project specific ProGuard rules here.
-keep class id.editor.newsapp.data.model.** { *; }
-keep class com.google.gson.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** { *; }

# Jsoup
-keep class org.jsoup.** { *; }
