package com.jesperdj.vecmath

fun checkIndex(k: Int, limit: Int): Int = if (k in 0 until limit) k else throw IndexOutOfBoundsException(k)
