plugins {
    alias(libs.plugins.sm.main.lib.gradle.plugin)
}

android {
    namespace = "ru.profitsw2000.mainscreen"
}

dependencies {
    implementation(project(":core"))
    //стандартные
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.androidx.room.compiler)
    //recyclerview
    implementation(libs.androidx.recyclerview)
    //fragment
    implementation(libs.androidx.fragment)
    //для ViewModel
    implementation(libs.androidx.lifecycle)
    //Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    //Koin
    implementation(libs.koin)
    //Data store
    implementation(libs.kotlinx.serialization)
    implementation(libs.datastore.preferences)
    //Тесты
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}