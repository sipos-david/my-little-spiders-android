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
import dev.shipi.mylittlespiders.domain.usecase.AddEntry
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.DeleteEntry
import dev.shipi.mylittlespiders.domain.usecase.DeleteFriend
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.GetFriendList
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendList
import dev.shipi.mylittlespiders.domain.usecase.UpdateEntry
import dev.shipi.mylittlespiders.domain.usecase.UpdateFriend
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Create data layer

    @Provides
    @Singleton
    fun provideDatabase(): FriendsDatabase = FriendsDatabaseMock()

    @Provides
    @Singleton
    fun provideApi(): FriendsApi = FriendsApiMock()

    @Provides
    @Singleton
    fun provideInteractor(db: FriendsDatabase, api: FriendsApi) = FriendsInteractor(api, db)

    // Create use cases

    @Provides
    @Singleton
    fun provideRefreshFriendList(interactor: FriendsInteractor) = RefreshFriendList(interactor)

    @Provides
    @Singleton
    fun provideGetFriendList(interactor: FriendsInteractor) = GetFriendList(interactor)


    @Provides
    @Singleton
    fun provideAddFriend(interactor: FriendsInteractor) = AddFriend(interactor)

    @Provides
    @Singleton
    fun provideAddEntry(interactor: FriendsInteractor) = AddEntry(interactor)

    @Provides
    @Singleton
    fun provideDeleteEntry(interactor: FriendsInteractor) = DeleteEntry(interactor)

    @Provides
    @Singleton
    fun provideDeleteFriend(interactor: FriendsInteractor) = DeleteFriend(interactor)

    @Provides
    @Singleton
    fun provideGetFriendDetails(interactor: FriendsInteractor) = GetFriendDetails(interactor)

    @Provides
    @Singleton
    fun provideRefreshFriendDetails(interactor: FriendsInteractor) =
        RefreshFriendDetails(interactor)

    @Provides
    @Singleton
    fun provideUpdateEntry(interactor: FriendsInteractor) = UpdateEntry(interactor)

    @Provides
    @Singleton
    fun provideUpdateFriend(interactor: FriendsInteractor) = UpdateFriend(interactor)

    @Provides
    @Singleton
    fun provideCheckNetworkState() = CheckNetworkState()

}