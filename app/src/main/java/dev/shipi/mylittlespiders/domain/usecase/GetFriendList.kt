package dev.shipi.mylittlespiders.domain.usecase

import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class GetFriendList(private val repository: FriendsRepository) {
    operator fun invoke() = repository.friends
}