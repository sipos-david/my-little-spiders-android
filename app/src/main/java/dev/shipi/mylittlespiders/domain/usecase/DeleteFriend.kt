package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class DeleteFriend(private val repository: FriendsRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteFriend(id)
    }
}