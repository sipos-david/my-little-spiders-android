package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.NewFriend
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val addFriend: AddFriend,
) : ViewModel() {

    val formViewModel =
        FriendFormViewModel.create("Add new roommate", "Let's become friends!", this::onSubmit)

    private suspend fun onSubmit(name: String, location: String, nightmares: Int) {
        addFriend(NewFriend(name, location, nightmares))
    }
}