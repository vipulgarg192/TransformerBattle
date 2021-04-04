package com.app.transformerbattle.di

import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerDtoMapper
import com.app.transformerbattle.repository.AppRepository
import com.app.transformerbattle.repository.AppRepository_Imple
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.modules.ApplicationContextModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideTransformerMapper(): TransformerDtoMapper {
        return TransformerDtoMapper()
    }

    @Singleton
    @Provides
    fun provideRecipeRepository(
        apiService: ApiService,
        transformerDtoMapper: TransformerDtoMapper,
    ): AppRepository{
        return AppRepository_Imple(
            apiService,
            transformerDtoMapper
        )
    }
}
