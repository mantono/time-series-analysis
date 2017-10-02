package com.mantono.tsa

fun <T: Number> Collection<T>.sum(): Double = sumByDouble { it.toDouble() }
fun <T: Number> Collection<T>.average(): Double = asSequence().map { it.toDouble() }.average()

fun <T: Number> Collection<T>.median(): Double
{
    val drop: Int = maxOf(Math.ceil(size / 2.0 - 1).toInt(), 0)
    val take: Int = 2 - (size % 2)

    return asSequence()
        .map { it.toDouble() }
        .sorted()
        .drop(drop)
        .take(take)
        .average()
}