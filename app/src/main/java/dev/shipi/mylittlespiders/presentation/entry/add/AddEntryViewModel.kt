package dev.shipi.mylittlespiders.presentation.entry.add

import androidx.lifecycle.ViewModel
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.usecase.AddEntry
import dev.shipi.mylittlespiders.presentation.entry.EntryFormViewModel
import java.util.Date

class AddEntryViewModel(
    private val friendId: Long,
    private val addEntry: AddEntry
) : ViewModel() {

    val formViewModel = EntryFormViewModel.create("Add new entry", "Add", this::onSubmit)

    private suspend fun onSubmit(date: Date, text: String, respect: Int) {
        addEntry(friendId, NewEntry(date, text, respect))
    }
}