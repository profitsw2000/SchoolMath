package ru.profitsw2000.mainscreen.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.profitsw2000.core.data.repository.PrimitiveTestSettingsRepositoryImpl
import ru.profitsw2000.core.domain.repository.PrimitiveTestSettingsRepository
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.mappers.PrimitiveTestResultMapper
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.mainscreen.data.data.repository.TestCategoryRepositoryImpl
import ru.profitsw2000.mainscreen.data.data.repository.TestHistoryRepositoryImpl
import ru.profitsw2000.mainscreen.data.domain.repository.TestCategoryRepository
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository
import ru.profitsw2000.mainscreen.data.domain.usecase.TestCategoryUseCase
import ru.profitsw2000.mainscreen.data.domain.usecase.TestHistoryUseCase
import ru.profitsw2000.mainscreen.presentation.viewmodel.MainViewModel

val mainModule = module {
    factory { PrimitiveTestResultMapper() }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("app_settings_pref") }
        )
    }
    single<TestCategoryRepository> { TestCategoryRepositoryImpl() }
    single<TestHistoryRepository> { TestHistoryRepositoryImpl(get(), get()) }
    single<PrimitiveTestSettingsRepository> { PrimitiveTestSettingsRepositoryImpl(get()) }
    single { TestCategoryUseCase(get()) }
    single { TestHistoryUseCase(get()) }
    single { PrimitiveTestSettingsUseCase(get()) }
    viewModel { MainViewModel(get(), get(), get()) }
}