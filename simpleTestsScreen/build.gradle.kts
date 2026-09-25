plugins {
    alias(libs.plugins.sm.main.lib.gradle.plugin)
}

android {
    namespace = "ru.profitsw2000.simpletestsscreen"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.kotlinx.serialization)
    implementation(libs.datastore.preferences)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}