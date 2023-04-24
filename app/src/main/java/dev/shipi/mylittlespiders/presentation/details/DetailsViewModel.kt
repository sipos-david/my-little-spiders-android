package dev.shipi.mylittlespiders.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<FriendDetails>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                try {
                    val id = 0L
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