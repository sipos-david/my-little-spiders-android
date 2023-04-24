package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class AddEntry(private val repository: FriendsRepository) {
    suspend operator fun invoke(friendId: Long, new: NewEntry) = repository.addEntry(friendId, new)
}