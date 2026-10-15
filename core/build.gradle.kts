plugins {
    alias(libs.plugins.sm.main.lib.gradle.plugin)
}

android {
    namespace = "ru.profitsw2000.core"
}
/*

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Или 17
    }
}
*/

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.paging)
    implementation(libs.kotlinx.serialization)
    //Тесты
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.io.mockk)
    testImplementation(libs.androidx.junit)
    kspTest(libs.androidx.room.compiler)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
