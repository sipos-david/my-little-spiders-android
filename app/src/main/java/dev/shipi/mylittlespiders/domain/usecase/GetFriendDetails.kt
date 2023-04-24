package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class GetFriendDetails(private val repository: FriendsRepository) {
    suspend operator fun invoke(id: Long) = repository.getFriendDetails(id)
}