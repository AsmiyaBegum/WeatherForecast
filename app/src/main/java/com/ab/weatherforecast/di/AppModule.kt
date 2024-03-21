package com.ab.weatherforecast.di

import android.app.Application
import androidx.room.Room
import com.ab.weatherforecast.BuildConfig
import com.ab.weatherforecast.data.data_source.WeatherForecastDatabase
import com.ab.weatherforecast.data.repository.AddressRepositoryImpl
import com.ab.weatherforecast.data.repository.FavoriteLocationRepositoryImpl
import com.ab.weatherforecast.data.service.AddressService
import com.ab.weatherforecast.data.service.WeatherService
import com.ab.weatherforecast.domain.repository.AddressRepository
import com.ab.weatherforecast.domain.repository.FavoriteLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Named("weatherRetrofit")
    fun provideWeatherRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .build()
    }

    @Provides
    @Named("addressRetrofit")
    fun provideAddressRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.ADDRESS_BASE_URL)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val newRequestBuilder = original.newBuilder()
                it.proceed(newRequestBuilder.build())
            }
            .callTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    fun provideAddressService(@Named("addressRetrofit") retrofit: Retrofit):  AddressService=
        retrofit.create(AddressService::class.java)

    @Provides
    fun provideWeatherService(@Named("weatherRetrofit") retrofit: Retrofit):  WeatherService=
        retrofit.create(WeatherService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideWeatherForecastDatabase(app: Application): WeatherForecastDatabase {
        return Room.databaseBuilder(app, WeatherForecastDatabase::class.java, WeatherForecastDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteLocationRepository(db: WeatherForecastDatabase): FavoriteLocationRepository {
        return FavoriteLocationRepositoryImpl(db.favoriteLocationDao)
    }
}
