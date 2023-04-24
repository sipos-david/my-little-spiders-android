package dev.shipi.mylittlespiders.presentation.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shipi.mylittlespiders.lib.presentation.Input
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class EntryFormViewModel(
    title: String,
    submit: String,
    private val onSubmit: suspend (name: Date, text: String, respect: Int) -> Unit
) : ViewModel() {

    private val date = Input.RequiredDate()
    private val text = Input.RequiredString()
    private val respect = Input.RequiredMinInt()

    private val _state =
        MutableStateFlow(
            EntryFormState(
                title,
                submit,
                date.input,
                text.input,
                respect.input
            )
        )
    val state = _state.asStateFlow()

    fun setDate(value: Date) {
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

    fun onSubmit() {
        viewModelScope.launch {
            if (hasErrors()) {
                onSubmit(
                    date.input.value ?: Date(),
                    text.input.value,
                    respect.input.value
                )
            }
        }
    }

    private fun hasErrors(): Boolean {
        return date.input.hasError || text.input.hasError || respect.input.hasError
    }

    companion object {
        fun create(
            title: String,
            submit: String,
            onSubmit: suspend (date: Date, text: String, respect: Int) -> Unit
        ): EntryFormViewModel {
            return EntryFormViewModel(title, submit, onSubmit)
        }
    }
}