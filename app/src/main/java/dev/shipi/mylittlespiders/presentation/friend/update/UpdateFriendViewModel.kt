package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel
class UpdateFriendViewModel @Inject constructor(
    private val updateFriend: UpdateFriend,
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<FriendFormViewModel>>(ViewState.Loading)
    val state = _state.asStateFlow()

    fun showFriendDetails(friendId: String?) {
        viewModelScope.launch {
            _state.update {
                try {
                    _state.value = ViewState.Loading

                    val id =
                        friendId?.toLongOrNull()
                            ?: throw Exception("Friend id missing or invalid format!")
                    val details =
                        getFriendDetails(id)
                            ?: throw Exception("Friend with id:$id not found!")

                    val formViewModel = FriendFormViewModel.create()

                    formViewModel.apply {
                        setId(details.id)
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

    fun updateFriend() {
        viewModelScope.launch {
            val viewState = _state.value
            if (viewState !is ViewState.Data || viewState.data.hasErrors()
            ) {
                return@launch
            }
            val data = viewState.data.state.value
            data.friendId?.let {
                EditFriend(
                    it,
                    data.name.value,
                    data.location.value,
                    data.nightmares.value ?: 0
                )
            }?.let {
                updateFriend(
                    it
                )
            }
        }
    }
}