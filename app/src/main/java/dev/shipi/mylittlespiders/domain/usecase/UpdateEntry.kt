package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class UpdateEntry(private val repository: FriendsRepository) {
    suspend operator fun invoke(edited: Entry) = repository.editEntry(edited)
}