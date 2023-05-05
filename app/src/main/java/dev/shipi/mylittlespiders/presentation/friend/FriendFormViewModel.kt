package dev.shipi.mylittlespiders.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.components.forms.Input
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendFormViewModel : ViewModel() {

    private val name = Input.RequiredString()
    private val location = Input.RequiredString()
    private val nightmares = Input.MinInt()

    private val _state =
        MutableStateFlow(
            FriendFormState(
                name.input,
                location.input,
                nightmares.input
            )
        )
    val state = _state.asStateFlow()

    fun setId(value: Long?) {
        viewModelScope.launch {
            _state.update {
                it.copy(friendId = value)
            }
        }
    }

    fun setName(value: String) {
        viewModelScope.launch {
            _state.update {
                name.set(value)
                it.copy(hasErrors = hasErrors(), name = name.input)
            }
        }
    }

    fun setLocation(value: String) {
        viewModelScope.launch {
            _state.update {
                location.set(value)
                it.copy(hasErrors = hasErrors(), location = location.input)
            }
        }
    }

    fun setNightmares(value: String) {
        viewModelScope.launch {
            _state.update {
                nightmares.set(value.toIntOrNull())
                it.copy(hasErrors = hasErrors(), nightmares = nightmares.input)
            }
        }
    }

    fun hasErrors(): Boolean {
        return name.input.hasError || location.input.hasError || nightmares.input.hasError
    }

    fun clear() {
        viewModelScope.launch {
            _state.update {
                name.set("")
                location.set("")
                nightmares.set(null)
                it.copy(
                    hasErrors = hasErrors(),
                    name = name.input,
                    location = location.input,
                    nightmares = nightmares.input
                )
            }
        }
    }

    companion object {
        fun create() = FriendFormViewModel()
    }
}