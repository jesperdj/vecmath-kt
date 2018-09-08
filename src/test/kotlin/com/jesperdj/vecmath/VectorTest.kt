package com.jesperdj.vecmath

import org.assertj.core.api.Assertions.*
import org.junit.Test

class VectorTest {

    @Test
    fun constructWithDimension() {
        val v = Vector(5)

        assertThat(v)
                .hasDimension(5)
                .hasIndices(0..4)
    }

    @Test
    fun constructWithDimensionAndInit() {
        val v = Vector(5) { (it + 1).toDouble() }

        assertThat(v).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
    }

    @Test
    fun copy() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = v1.copy()

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)

        v2 += 7.5

        assertThat(v1).describedAs("Modifying v2 should not modify v1")
                .hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
    }

    @Test
    fun getChecksIndexBounds() {
        val v = Vector(5)

        assertThatThrownBy { v[-1] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { v[5] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 5")
    }

    @Test
    fun setChecksIndexBounds() {
        val v = Vector(5)

        assertThatThrownBy { v[-1] = 0.0 }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { v[5] = 0.0 }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 5")
    }

    @Test
    fun iterator() {
        val v = Vector(3) { (it + 1).toDouble() }

        var count = 0
        for (e in v) {
            assertThat(e).isEqualTo((++count).toDouble())
        }
        assertThat(count).isEqualTo(3)

        val iter = v.iterator()
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(1.0)
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(2.0)
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(3.0)
        assertThat(iter.hasNext()).isFalse()
        assertThatThrownBy { iter.next() }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 3")
    }

    @Test
    fun unaryPlus() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = +v1

        assertThat(v2).isEqualTo(v1)
    }

    @Test
    fun unaryMinus() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = -v1

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(-1.0, -2.0, -3.0, -4.0, -5.0)
    }

    @Test
    fun plusScalar() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = v1 + 1.5
        val v3 = 1.5 + v1

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(2.5, 3.5, 4.5, 5.5, 6.5)
        assertThat(v3).isEqualTo(v2)
    }

    @Test
    fun minusScalar() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = v1 - 1.5

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(-0.5, 0.5, 1.5, 2.5, 3.5)
    }

    @Test
    fun timesScalar() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = v1 * 1.5
        val v3 = 1.5 * v1

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(1.5, 3.0, 4.5, 6.0, 7.5)
        assertThat(v3).isEqualTo(v2)
    }

    @Test
    fun divScalar() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = v1 / 2.0

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(0.5, 1.0, 1.5, 2.0, 2.5)
    }

    @Test
    fun plusAssignScalar() {
        val v = Vector(5) { (it + 1).toDouble() }
        v += 1.5

        assertThat(v).hasElements(2.5, 3.5, 4.5, 5.5, 6.5)
    }

    @Test
    fun minusAssignScalar() {
        val v = Vector(5) { (it + 1).toDouble() }
        v -= 1.5

        assertThat(v).hasElements(-0.5, 0.5, 1.5, 2.5, 3.5)
    }

    @Test
    fun timesAssignScalar() {
        val v = Vector(5) { (it + 1).toDouble() }
        v *= 1.5

        assertThat(v).hasElements(1.5, 3.0, 4.5, 6.0, 7.5)
    }

    @Test
    fun divAssignScalar() {
        val v = Vector(5) { (it + 1).toDouble() }
        v /= 2.0

        assertThat(v).hasElements(0.5, 1.0, 1.5, 2.0, 2.5)
    }

    @Test
    fun plusVector() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = Vector(-3.0, 2.0, 5.0, 1.0, -4.0)
        val v3 = v1 + v2

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(-3.0, 2.0, 5.0, 1.0, -4.0)
        assertThat(v3).hasElements(-2.0, 4.0, 8.0, 5.0, 1.0)
    }

    @Test
    fun minusVector() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = Vector(-3.0, 2.0, 5.0, 1.0, -4.0)
        val v3 = v1 - v2

        assertThat(v1).hasElements(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(v2).hasElements(-3.0, 2.0, 5.0, 1.0, -4.0)
        assertThat(v3).hasElements(4.0, 0.0, -2.0, 3.0, 9.0)
    }

    @Test
    fun plusAssignVector() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = Vector(-3.0, 2.0, 5.0, 1.0, -4.0)
        v1 += v2

        assertThat(v1).hasElements(-2.0, 4.0, 8.0, 5.0, 1.0)
        assertThat(v2).hasElements(-3.0, 2.0, 5.0, 1.0, -4.0)
    }

    @Test
    fun minusAssignVector() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = Vector(-3.0, 2.0, 5.0, 1.0, -4.0)
        v1 -= v2

        assertThat(v1).hasElements(4.0, 0.0, -2.0, 3.0, 9.0)
        assertThat(v2).hasElements(-3.0, 2.0, 5.0, 1.0, -4.0)
    }

    @Test
    fun timesMatrix() {
        val v = Vector(-0.5, 1.5, 2.5) * Matrix(3, 2) { i, j -> (i * 2 + j + 1).toDouble() }

        assertThat(v).hasElements(16.5, 20.0)
    }

    @Test
    fun dot() {
        val v1 = Vector(5) { (it + 1).toDouble() }
        val v2 = Vector(-3.0, 2.5, 5.0, 4.0, -4.0)

        assertThat(v1 dot v2).isEqualTo(13.0)
    }

    @Test
    fun magnitude() {
        val v = Vector(3.0, 4.0)

        assertThat(v.magnitude).isEqualTo(5.0)
    }

    @Test
    fun normalized() {
        val v1 = Vector(5.0, 12.0)
        val v2 = v1.normalized()

        assertThat(v1).hasElements(5.0, 12.0)
        assertThat(v2).hasElements(5.0 / 13.0, 12.0 / 13.0)
        assertThat(v2.magnitude).isEqualTo(1.0)
    }
}
