package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.UpdateFriend
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateFriendViewModel(
    private val friendId: Long,
    private val updateFriend: UpdateFriend,
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<FriendFormViewModel>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                try {
                    val details =
                        getFriendDetails(friendId)
                            ?: throw Exception("Friend with id:$friendId not found!")

                    val formViewModel =
                        FriendFormViewModel.create(
                            "Edit roommate",
                            "Ok"
                        ) { name, location, nightmares ->
                            updateFriend(EditFriend(friendId, name, location, nightmares))
                        }

                    formViewModel.apply {
                        setName(details.name)
                        setLocation(details.location)
                        setNightmares(details.nightmares.toString())
                    }

                    ViewState.Data(formViewModel, checkNetworkState())
                } catch (e: Exception) {
                    ViewState.Error(e)
                }
            }
        }
    }
}