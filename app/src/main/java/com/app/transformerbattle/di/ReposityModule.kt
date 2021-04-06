package com.app.transformerbattle.di

import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.domain.abstraction.AppRepository
import com.app.transformerbattle.repository.AppRepository_Imple
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        apiService: ApiService): AppRepository {
        return AppRepository_Imple(
            apiService
        )
    }
}
