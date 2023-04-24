package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.NewFriend
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class AddFriend(private val repository: FriendsRepository) {
    suspend operator fun invoke(new: NewFriend) = repository.addFriend(new)
}