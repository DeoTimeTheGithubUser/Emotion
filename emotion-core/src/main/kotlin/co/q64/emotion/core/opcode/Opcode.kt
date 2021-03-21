package co.q64.emotion.core.opcode

import co.q64.emotion.core.lang.Stack
import co.q64.emotion.core.value.ValueType

data class Opcode(
    val id: String,
    val values: List<ValueType>,
    val marker: OpcodeMarker? = null,
    val description: String,
    val closure: Stack.() -> Unit
)