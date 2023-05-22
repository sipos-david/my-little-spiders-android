package dev.shipi.mylittlespiders.data.network.fake

import dev.shipi.mylittlespiders.data.network.client.models.Roommate


data class FakeDb(
    val roommates: MutableList<Roommate> = mutableListOf()
) {
    private var _id: Long = 0
    val id
        get() = _id++

    fun clear() {
        _id = 0
        roommates.clear()
    }
}