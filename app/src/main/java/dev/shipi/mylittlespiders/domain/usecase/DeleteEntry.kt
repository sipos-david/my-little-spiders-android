package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class DeleteEntry(private val repository: FriendsRepository) {
    suspend operator fun invoke(friendId: Long, entryId: Long) =
        repository.deleteEntry(friendId, entryId)
}