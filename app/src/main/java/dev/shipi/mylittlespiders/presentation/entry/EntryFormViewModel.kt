package dev.shipi.mylittlespiders.presentation.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.components.forms.Input
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EntryFormViewModel(friendId: Long) : ViewModel() {

    private val date = Input.RequiredDate()
    private val text = Input.RequiredString()
    private val respect = Input.RequiredMinInt()

    private val _state =
        MutableStateFlow(
            EntryFormState(
                friendId,
                date.input,
                text.input,
                respect.input
            )
        )
    val state = _state.asStateFlow()

    fun setId(value: Long?) {
        viewModelScope.launch {
            _state.update {
                it.copy(entryId = value)
            }
        }
    }

    fun setDate(value: LocalDate) {
        viewModelScope.launch {
            _state.update {
                date.set(value)
                it.copy(hasErrors = hasErrors(), date = date.input)
            }
        }
    }

    fun setText(value: String) {
        viewModelScope.launch {
            _state.update {
                text.set(value)
                it.copy(hasErrors = hasErrors(), text = text.input)
            }
        }
    }

    fun setRespect(value: String) {
        viewModelScope.launch {
            _state.update {
                respect.set(value.toIntOrNull() ?: 0)
                it.copy(hasErrors = hasErrors(), respect = respect.input)
            }
        }
    }

    fun hasErrors(): Boolean {
        return date.input.hasError || text.input.hasError || respect.input.hasError
    }

    companion object {
        fun create(friendId: Long) = EntryFormViewModel(friendId)
    }
}