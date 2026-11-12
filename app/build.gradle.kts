plugins { id("com.android.application") }
android {
 namespace = "com.bitcoin.walletwidget"
 compileSdk = 35
 defaultConfig { applicationId = "com.bitcoin.walletwidget"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0-alpha" }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
