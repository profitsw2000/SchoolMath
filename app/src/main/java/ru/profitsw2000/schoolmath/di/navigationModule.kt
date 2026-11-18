package ru.profitsw2000.schoolmath.di

import org.koin.dsl.module
import ru.profitsw2000.navigator.Navigator
import ru.profitsw2000.schoolmath.navigation.AppNavigator

val navigationModule = module {
    single { AppNavigator() }
    single<Navigator> { get<AppNavigator>() }
}