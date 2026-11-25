plugins {
    alias(libs.plugins.sm.main.app.gradle.plugin)
}

android {
    namespace = "ru.profitsw2000.schoolmath"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":mainScreen"))
    implementation(project(":settingsScreen"))
    implementation(project(":simpleTestsScreen"))
    implementation(project(":navigator"))

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.material)
    //Koin
    implementation(libs.koin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

tasks.withType<Test> {
    systemProperty("net.bytebuddy.experimental", "true")
}