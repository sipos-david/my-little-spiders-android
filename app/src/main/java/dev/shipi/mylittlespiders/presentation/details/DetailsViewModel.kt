package dev.shipi.mylittlespiders.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.DeleteEntry
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.RefreshFriendDetails
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getFriendDetails: GetFriendDetails,
    private val refreshFriendDetails: RefreshFriendDetails,
    private val deleteEntry: DeleteEntry,
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

    fun onDeleteEntry(entryId: Long) {
        viewModelScope.launch {
            if (_state.value !is ViewState.Data) {
                return@launch
            }
            val details = (_state.value as ViewState.Data<FriendDetails>).data

            _state.update { ViewState.Loading }
            try {
                deleteEntry(details.id, entryId)

                _state.update {
                    val updated = refreshFriendDetails(details.id) ?: return@update ViewState.Error(
                        Exception("Cannot delete entry")
                    )
                    ViewState.Data(updated, checkNetworkState())
                }
            } catch (e: Exception) {
                _state.update {
                    ViewState.Error(e)
                }
            }
        }
    }
}