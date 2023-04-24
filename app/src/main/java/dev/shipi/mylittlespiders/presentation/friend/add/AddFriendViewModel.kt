package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.NewFriend
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val addFriend: AddFriend,
) : ViewModel() {

    val formViewModel =
        FriendFormViewModel.create()

    fun addFriend() {
        viewModelScope.launch {
            if (formViewModel.hasErrors()) {
                return@launch
            }
            val data = formViewModel.state.value
            addFriend(
                NewFriend(
                    data.name.value,
                    data.location.value,
                    data.nightmares.value ?: 0
                )
            )
            formViewModel.clear()
        }
    }
}