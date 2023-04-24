package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class GetFriendDetails(private val repository: FriendsRepository) {
    suspend operator fun invoke(id: Long): FriendDetails? {
        return repository.getFriendDetails(id)
    }
}