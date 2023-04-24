package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class RefreshFriendList(private val repository: FriendsRepository) {
    suspend operator fun invoke() {
        repository.refreshFriends()
    }
}