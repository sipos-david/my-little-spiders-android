package dev.shipi.mylittlespiders.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.lib.presentation.Input
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendFormViewModel(
    title: String,
    submit: String,
    private val onSubmit: suspend (name: String, location: String, nightmares: Int) -> Unit
) : ViewModel() {

    private val name = Input.RequiredString()
    private val location = Input.RequiredString()
    private val nightmares = Input.MinInt()

    private val _state =
        MutableStateFlow(
            FriendFormState(
                title,
                submit,
                name.input,
                location.input,
                nightmares.input
            )
        )
    val state = _state.asStateFlow()

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

    fun onSubmit() {
        viewModelScope.launch {
            if (hasErrors()) {
                onSubmit(
                    name.input.value,
                    location.input.value,
                    nightmares.input.value ?: 0
                )
            }
        }
    }

    private fun hasErrors(): Boolean {
        return name.input.hasError || location.input.hasError || nightmares.input.hasError
    }

    companion object {
        fun create(
            title: String,
            submit: String,
            onSubmit: suspend (name: String, location: String, nightmares: Int) -> Unit
        ): FriendFormViewModel {
            return FriendFormViewModel(title, submit, onSubmit)
        }
    }
}