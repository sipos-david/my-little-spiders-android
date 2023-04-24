package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class DeleteEntry(private val repository: FriendsRepository) {
    suspend operator fun invoke(friendId: Long, entry: Entry) {
        repository.deleteEntry(friendId, entry.id)
    }
}