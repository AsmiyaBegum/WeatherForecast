package com.ab.weatherforecast.di

import com.ab.weatherforecast.data.repository.AddressRepositoryImpl
import com.ab.weatherforecast.data.repository.WeatherRepositoryImpl
import com.ab.weatherforecast.domain.repository.AddressRepository
import com.ab.weatherforecast.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun addressRepository(addressRepositoryImpl :AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun weatherRepository(weatherRepositoryImpl :WeatherRepositoryImpl): WeatherRepository
}