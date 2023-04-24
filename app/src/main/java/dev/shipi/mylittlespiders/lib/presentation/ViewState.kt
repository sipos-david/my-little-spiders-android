package dev.shipi.mylittlespiders.lib.presentation


sealed class ViewState<out T> {
    data class Data<out T>(val data: T, val isNetworkAvailable: Boolean) : ViewState<T>()
    data class Error(val e: Exception) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
}