package dev.shipi.mylittlespiders.lib

fun createString(int: Int?): String {
    if (int != null) {
        return "$int"
    }
    return ""
}