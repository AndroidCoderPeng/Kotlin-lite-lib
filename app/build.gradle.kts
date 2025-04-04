plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.pengxh.kt.lib"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pengxh.kt.lib"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":lite"))
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    //Google官方授权框架
    implementation("pub.devrel:easypermissions:3.0.0")
    //沉浸式状态栏
    implementation("com.gyf.immersionbar:immersionbar:3.0.0")
    implementation("com.gyf.immersionbar:immersionbar-components:3.0.0")
    implementation("com.google.android.material:material:1.10.0")
    //图片选择框架
    implementation("io.github.lucksiege:pictureselector:v3.11.1")
    //图片加载库
    implementation("com.github.bumptech.glide:glide:4.12.0")
    //大图
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    //协程核心包
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    //CameraX
    // CameraX PreviewView
    implementation("androidx.camera:camera-view:1.2.3")
    // CameraX Lifecycle ProcessCameraProvider
    implementation("androidx.camera:camera-lifecycle:1.2.3")
    // CameraX Camera2 extensions(CameraX is not configured properly. The most likely cause is you did not include a default implementation in your build such as "camera-camera2".)
    implementation("androidx.camera:camera-camera2:1.2.3")
    //官方Json解析库
    implementation("com.google.code.gson:gson:2.10.1")
    //网络请求和接口封装
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //图片压缩
    implementation("top.zibin:Luban:1.1.8")
}