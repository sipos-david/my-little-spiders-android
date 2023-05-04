package dev.shipi.mylittlespiders.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.DeleteFriend
import dev.shipi.mylittlespiders.domain.usecase.GetFriendList
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendList
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val refreshFriendList: RefreshFriendList,
    private val getFriendList: GetFriendList,
    private val deleteFriend: DeleteFriend,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<Friend>>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFriendList().collect { list ->
                _state.update { ViewState.Data(list, checkNetworkState()) }
            }
        }
    }

    fun refreshList() {
        viewModelScope.launch {
            refreshFriendList()
        }
    }

    fun onDeleteFriend(friendId: Long) {
        viewModelScope.launch {
            deleteFriend(friendId)
        }
    }
}