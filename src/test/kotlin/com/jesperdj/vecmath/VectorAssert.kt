package com.jesperdj.vecmath

import org.assertj.core.api.AbstractAssert

class VectorAssert(actual: Vector?) : AbstractAssert<VectorAssert, Vector>(actual, VectorAssert::class.java) {

    fun hasDimension(dim: Int): VectorAssert {
        isNotNull()
        if (actual.dimension != dim) {
            failWithMessage("Expected vector of dimension <$dim> but was <${actual.dimension}>")
        }
        return this
    }

    fun hasIndices(indices: IntRange): VectorAssert {
        isNotNull()
        if (actual.indices != indices) {
            failWithMessage("Expected vector with indices <$indices> but was <${actual.indices}>")
        }
        return this
    }

    fun hasElements(vararg elems: Double): VectorAssert {
        hasDimension(elems.size)

        var error = false
        val sb1 = StringBuilder().append('[')
        val sb2 = StringBuilder().append('[')
        for (k in elems.indices) {
            if (k > 0) {
                sb1.append(' ')
                sb2.append(' ')
            }
            if (actual[k] != elems[k]) {
                error = true
                sb1.append('<').append(elems[k]).append('>')
                sb2.append('<').append(actual[k]).append('>')
            } else {
                sb1.append(elems[k])
                sb2.append(actual[k])
            }
        }
        sb1.append(']')
        sb2.append(']')

        if (error) {
            failWithMessage("Actual vector elements differ from expected elements\n" +
                    "Expected: $sb1\n" +
                    "Actual:   $sb2")
        }

        return this
    }
}

fun assertThat(v: Vector) = VectorAssert(v)
