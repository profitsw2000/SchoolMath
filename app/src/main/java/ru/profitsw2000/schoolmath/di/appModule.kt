package ru.profitsw2000.schoolmath.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.profitsw2000.core.room.database.AppDatabase
import ru.profitsw2000.navigator.Navigator
import ru.profitsw2000.schoolmath.navigation.AppNavigator

val appModule = module {
    single {
        AppDatabase.create(androidContext())
        AppDatabase.getInstance()
    }
    single { AppNavigator() }
    single<Navigator> { get<AppNavigator>() }
}