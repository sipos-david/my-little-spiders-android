package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class UpdateFriend(private val repository: FriendsRepository) {
    suspend operator fun invoke(edited: EditFriend) = repository.editFriend(edited)
}