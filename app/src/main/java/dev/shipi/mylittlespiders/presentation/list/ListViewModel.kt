package dev.shipi.mylittlespiders.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.usecase.DeleteFriend
import dev.shipi.mylittlespiders.domain.usecase.GetFriendList
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendList
import dev.shipi.mylittlespiders.components.ViewState
import dev.shipi.mylittlespiders.services.NetworkObserver
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
    private val networkObserver: NetworkObserver,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<Friend>>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "Roommate list")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "roommate list")
            }

            launch {
                getFriendList().collect { list ->
                    _state.update { ViewState.Data(list, networkObserver.isConnected) }
                }
            }

            launch {
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