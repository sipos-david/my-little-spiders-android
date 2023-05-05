package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.UpdateFriend
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel
import dev.shipi.mylittlespiders.services.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateFriendViewModel @Inject constructor(
    private val updateFriend: UpdateFriend,
    private val getFriendDetails: GetFriendDetails,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<FriendFormViewModel>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            networkObserver.networkAvailable.collect { network ->
                _state.update {
                    if (it is ViewState.Data) {
                        it.copy(network.toBoolean())
                    } else {
                        it
                    }
                }
            }
        }
    }

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

                    ViewState.Data(formViewModel, networkObserver.isConnected)
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