package dev.shipi.mylittlespiders.components.forms

import java.time.LocalDate

sealed class Input<T>(
    value: T,
    hasError: Boolean,
    private val rule: (T) -> Boolean
) {
    private var _value = Value(value, hasError)
    val input: Value<T>
        get() = _value

    fun set(value: T): Value<T> {
        _value = Value(value, rule(value))
        return _value
    }

    data class Value<T>(val value: T, val hasError: Boolean)


    class RequiredString(initial: String = "") :
        Input<String>(initial, initial.isBlank(), { it.isBlank() })

    class MinInt(private val min: Int = 0, initial: Int? = null) :
        Input<Int?>(initial, initial != null && initial < min, { it != null && it < min })

    class RequiredMinInt(private val min: Int = 0, initial: Int = 0) :
        Input<Int>(initial, initial < min, { it < min })

    class RequiredDate(initial: LocalDate? = null) :
        Input<LocalDate?>(initial, initial == null, { it == null })
}