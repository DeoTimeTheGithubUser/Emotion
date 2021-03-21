package co.q64.emotion.core.opcode.standard

import co.q64.emotion.core.math.rational
import co.q64.emotion.core.opcode.OpcodeRegistry
import co.q64.emotion.core.value.number

object NumberOpcodes : OpcodeRegistry {
    override fun register() {
        "number.increment"(number) { push(pop().number.add(1.rational())) }
    }
}