import java.text.Normalizer

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Os GIFs com espaços/acentos não podem ficar em res/drawable.
val prepararGifsExercicios by tasks.registering {
    doLast {
        val drawableDir = file("src/main/res/drawable")
        val assetsDir = file("src/main/assets/exercise_gifs")
        drawableDir.listFiles()?.filter { it.isFile && it.extension.equals("gif", ignoreCase = true) }?.forEach { gif ->
            val base = Normalizer.normalize(gif.nameWithoutExtension, Normalizer.Form.NFD)
                .replace(Regex("\\p{Mn}+"), "")
                .replace(Regex("[^A-Za-z0-9]+"), "_")
                .trim('_').lowercase()
            assetsDir.mkdirs()
            gif.copyTo(File(assetsDir, "$base.gif"), overwrite = true)
            gif.delete()
        }
    }
}
tasks.named("preBuild").configure { dependsOn(prepararGifsExercicios) }

android {
    namespace = "com.danielmarkpsn.controlecorporal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.danielmarkpsn.controlecorporal"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "1.2.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // ✅ Só configura signing se as variáveis do Codemagic existirem
    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("CM_KEYSTORE_PATH")
            if (!keystorePath.isNullOrBlank()) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("CM_KEY_ALIAS")
                keyPassword = System.getenv("CM_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // ✅ Aplica signing só se o keystore foi configurado
            val keystorePath = System.getenv("CM_KEYSTORE_PATH")
            if (!keystorePath.isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true  // ✅ ADICIONADO — resolve o aviso e evita erros de BuildConfig
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    ksp(libs.androidx.room.compiler)

    debugImplementation(libs.androidx.ui.tooling)
}
