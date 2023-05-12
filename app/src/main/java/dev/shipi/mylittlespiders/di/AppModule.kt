package dev.shipi.mylittlespiders.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shipi.mylittlespiders.data.FriendsInteractor
import dev.shipi.mylittlespiders.data.local.FriendsDatabase
import dev.shipi.mylittlespiders.data.local.FriendsDatabaseLocal
import dev.shipi.mylittlespiders.data.local.dao.EntryDao
import dev.shipi.mylittlespiders.data.local.dao.RoommateDao
import dev.shipi.mylittlespiders.data.local.db.AppDatabase
import dev.shipi.mylittlespiders.data.network.FriendsApi
import dev.shipi.mylittlespiders.data.network.FriendsApiNetwork
import dev.shipi.mylittlespiders.data.network.client.apis.EntriesApi
import dev.shipi.mylittlespiders.data.network.client.apis.RoommateApi
import dev.shipi.mylittlespiders.data.network.client.auth.ApiKeyAuth
import dev.shipi.mylittlespiders.data.network.client.infrastructure.ApiClient
import dev.shipi.mylittlespiders.domain.usecase.AddEntry
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.domain.usecase.DeleteEntry
import dev.shipi.mylittlespiders.domain.usecase.DeleteFriend
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.GetFriendList
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendList
import dev.shipi.mylittlespiders.domain.usecase.UpdateEntry
import dev.shipi.mylittlespiders.domain.usecase.UpdateFriend
import dev.shipi.mylittlespiders.services.NetworkObserver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Create analytics

    @Provides
    @Singleton
    fun provideAnalytics() = Firebase.analytics

    //  Create persistence layer
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, "friends-database"
    ).build()

    @Provides
    @Singleton
    fun provideRoommateDao(appDatabase: AppDatabase) = appDatabase.roommateDao()

    @Provides
    @Singleton
    fun provideEntryDao(appDatabase: AppDatabase) = appDatabase.entryDao()

    @Provides
    @Singleton
    fun provideDatabase(roommateDao: RoommateDao, entryDao: EntryDao): FriendsDatabase =
        FriendsDatabaseLocal(roommateDao, entryDao)

    // Create network layer

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        // Android emulator's IP address to host's localhost
        val apiClient = ApiClient(baseUrl = "http://10.0.2.2:8080")
        apiClient.addAuthorization(
            "apiKey",
            ApiKeyAuth("header", "Api-Key", "123456")
        )
        return apiClient
    }

    @Provides
    @Singleton
    fun provideRoommateApi(apiClient: ApiClient) = apiClient.createService(RoommateApi::class.java)

    @Provides
    @Singleton
    fun provideEntryApi(apiClient: ApiClient) = apiClient.createService(EntriesApi::class.java)

    @Provides
    @Singleton
    fun provideApi(roommateApi: RoommateApi, entriesApi: EntriesApi): FriendsApi =
        FriendsApiNetwork(roommateApi, entriesApi)

    // Create data layer

    @Provides
    @Singleton
    fun provideInteractor(db: FriendsDatabase, api: FriendsApi) = FriendsInteractor(api, db)

    // Create services

    @Provides
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context) = NetworkObserver(context)

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
}