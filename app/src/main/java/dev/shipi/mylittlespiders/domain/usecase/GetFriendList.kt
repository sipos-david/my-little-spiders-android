package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class GetFriendList(private val repository: FriendsRepository) {
    suspend operator fun invoke(): List<Friend> {
        return repository.getFriends()
    }
}