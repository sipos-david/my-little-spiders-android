package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.NewFriend
import dev.shipi.mylittlespiders.domain.usecase.AddFriend
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel
import dev.shipi.mylittlespiders.services.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val addFriend: AddFriend,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<FriendFormViewModel>>(
        ViewState.Data(
            FriendFormViewModel.create(),
            networkObserver.isConnected
        )
    )
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

    fun addFriend() {
        viewModelScope.launch {
            val viewState = _state.value
            if (viewState !is ViewState.Data || viewState.data.hasErrors()
            ) {
                return@launch
            }
            val data = viewState.data.state.value
            addFriend(
                NewFriend(
                    data.name.value,
                    data.location.value,
                    data.nightmares.value ?: 0
                )
            )
            viewState.data.clear()
        }
    }
}