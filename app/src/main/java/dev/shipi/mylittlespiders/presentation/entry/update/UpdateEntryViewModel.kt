package dev.shipi.mylittlespiders.presentation.entry.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UpdateEntryViewModel @Inject constructor(
    private val updateEntry: UpdateEntry,
    private val getFriendDetails: GetFriendDetails,
    private val checkNetworkState: CheckNetworkState
) : ViewModel() {
    private val _state = MutableStateFlow<ViewState<EntryFormViewModel>>(ViewState.Loading)
    val state = _state.asStateFlow()

    fun showEntryDetails(friendId: String?, entryId: String?) {
        viewModelScope.launch {
            _state.update {
                try {
                    _state.value = ViewState.Loading

                    val fId =
                        friendId?.toLongOrNull()
                            ?: throw Exception("Friend id missing or invalid format!")
                    val eId =
                        entryId?.toLongOrNull()
                            ?: throw Exception("Entry id missing or invalid format!")

                    val details =
                        getFriendDetails(fId)
                            ?: throw Exception("Friend with id:$fId not found!")

                    val edited = details.entries.firstOrNull { it.id == eId }
                        ?: throw Exception("Entry with id:$eId not found on friend!")

                    val formViewModel = EntryFormViewModel.create(fId)

                    formViewModel.setId(eId)
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

    fun updateEntry() {
        viewModelScope.launch {
            val viewState = _state.value
            if (viewState !is ViewState.Data || viewState.data.hasErrors()
            ) {
                return@launch
            }
            val data = viewState.data.state.value
            data.entryId?.let {
                Entry(
                    it,
                    data.date.value ?: LocalDate.now(),
                    data.text.value,
                    data.respect.value
                )
            }?.let {
                updateEntry(
                    it
                )
            }
        }
    }
}