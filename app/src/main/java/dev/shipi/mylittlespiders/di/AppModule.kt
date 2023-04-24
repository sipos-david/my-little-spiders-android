package dev.shipi.mylittlespiders.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shipi.mylittlespiders.data.FriendsInteractor
import dev.shipi.mylittlespiders.data.local.FriendsDatabase
import dev.shipi.mylittlespiders.data.local.FriendsDatabaseMock
import dev.shipi.mylittlespiders.data.network.FriendsApi
import dev.shipi.mylittlespiders.data.network.FriendsApiMock
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.GetFriendList
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendList
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Create data layer

    @Provides
    @Singleton
    fun provideDatabase(): FriendsDatabase {
        return FriendsDatabaseMock()
    }

    @Provides
    @Singleton
    fun provideApi(): FriendsApi {
        return FriendsApiMock()
    }

    @Provides
    @Singleton
    fun provideInteractor(db: FriendsDatabase, api: FriendsApi): FriendsInteractor {
        return FriendsInteractor(api, db)
    }

    // Create use cases

    @Provides
    @Singleton
    fun provideRefreshFriendList(interactor: FriendsInteractor): RefreshFriendList {
        return RefreshFriendList(interactor)
    }

    @Provides
    @Singleton
    fun provideGetFriendList(interactor: FriendsInteractor): GetFriendList {
        return GetFriendList(interactor)
    }

    @Provides
    @Singleton
    fun provideAddFriend(interactor: FriendsInteractor): AddFriend {
        return AddFriend(interactor)
    }

    @Provides
    @Singleton
    fun provideCheckNetworkState(): CheckNetworkState {
        return CheckNetworkState()
    }
}