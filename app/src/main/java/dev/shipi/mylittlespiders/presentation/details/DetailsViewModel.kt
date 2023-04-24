package dev.shipi.mylittlespiders.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {
    private val _state = MutableStateFlow<ViewState<FriendDetails>>(ViewState.Loading)
    val state = _state.asStateFlow()

    fun showFriendDetails(friendId: String?) {
        viewModelScope.launch {
            _state.update {
                try {
                    val id =
                        friendId?.toLongOrNull()
                            ?: throw Exception("Friend id missing or invalid format!")
                    val details =
                        getFriendDetails(id) ?: throw Exception("Friend with id:$id not found!")
                    ViewState.Data(details, checkNetworkState())
                } catch (e: Exception) {
                    ViewState.Error(e)
                }
            }
        }
    }
}