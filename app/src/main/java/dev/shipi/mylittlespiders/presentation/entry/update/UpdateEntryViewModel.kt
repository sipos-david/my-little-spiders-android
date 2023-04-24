package dev.shipi.mylittlespiders.presentation.entry.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.usecase.CheckNetworkState
import dev.shipi.mylittlespiders.domain.usecase.GetFriendDetails
import dev.shipi.mylittlespiders.domain.usecase.UpdateEntry
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.entry.EntryFormViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateEntryViewModel(
    private val friendId: Long,
    private val entryId: Long,
    private val updateEntry: UpdateEntry,
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {
    private val _state = MutableStateFlow<ViewState<EntryFormViewModel>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                try {
                    val details =
                        getFriendDetails(friendId)
                            ?: throw Exception("Friend with id:$friendId not found!")

                    val edited = details.entries.firstOrNull { it.id == entryId }
                        ?: throw Exception("Entry with id:$entryId not found on friend!")

                    val formViewModel =
                        EntryFormViewModel.create(
                            "Edit entry",
                            "Ok"
                        ) { date, text, respect ->
                            updateEntry(Entry(edited.id, date, text, respect))
                        }

                    formViewModel.apply {
                        setDate(edited.date)
                        setText(edited.text)
                        setRespect(edited.respect.toString())
                    }

                    ViewState.Data(formViewModel, checkNetworkState())
                } catch (e: Exception) {
                    ViewState.Error(e)
                }
            }
        }
    }
}