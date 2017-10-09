package com.mantono.tsa

import java.time.Instant

fun <T: Number> Iterable<T>.sum(): Double = sumByDouble { it.toDouble() }
fun <T: Number> Iterable<T>.average(): Double = asSequence().map { it.toDouble() }.average()

fun <T: Number> Collection<T>.median(): Double
{
    val first: Int = maxOf(Math.ceil(size / 2.0 - 1).toInt(), 0)
    val last: Int = first + (2 - (size % 2)) - 1

    return asSequence()
        .map { it.toDouble() }
        .sorted()
		.subList(first..last)
        .average()
}

fun <T: Number> Iterable<DataPoint<T>>.sortedSubList(include: IntRange): List<DataPoint<T>>
{
	return asSequence()
		.sortedBy { it.timestamp }
		.subList(include)
}

fun <T> Iterable<T>.subList(include: IntRange): List<T> = asSequence().subList(include)
fun <T> Sequence<T>.subList(include: IntRange): List<T> = filterIndexed { i, _ -> i in include }.toList()
fun <T> Sequence<T>.subSequence(include: IntRange): Sequence<T> = filterIndexed { i, _ -> i in include }

fun <T: Number> Sequence<Pair<Instant, T>>.asDataPoints() = this.map { DoubleDataPoint(it.first, it.second.toDouble()) }

data class DoubleDataPoint(override val timestamp: Instant, override val value: Double): DataPoint<Double>