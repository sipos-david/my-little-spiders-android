package dev.shipi.mylittlespiders.data.local.fake

import dev.shipi.mylittlespiders.data.local.model.Entry
import dev.shipi.mylittlespiders.data.local.model.Roommate

data class FakeDb(
    val entries: MutableList<Entry> = mutableListOf(),
    val roommates: MutableList<Roommate> = mutableListOf()
) {
    fun clear() {
        roommates.clear()
        entries.clear()
    }
}