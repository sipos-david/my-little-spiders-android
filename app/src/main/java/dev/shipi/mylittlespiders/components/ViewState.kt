package dev.shipi.mylittlespiders.components


sealed class ViewState<out T> {
    data class Data<out T>(val data: T, val isNetworkAvailable: Boolean) : ViewState<T>() {
        fun copy(isNetworkAvailable: Boolean) = Data(data, isNetworkAvailable)
    }

    data class Error(val e: Exception) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
}