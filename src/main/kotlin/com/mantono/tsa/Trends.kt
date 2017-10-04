package com.mantono.tsa

import java.time.Duration
import java.time.Instant

data class Trend(val first: Collection<Number>, val second: Collection<Number>)
{
    fun bySum(): Double = second.sum() / first.sum()
    fun byAverage(): Double = second.average() / first.average()
    fun byMedian(): Double = second.median() / first.median()
}

fun <T: Number> trend(data: Collection<DataPoint<T>>, length: Duration, stop: Instant = Instant.now()): Trend
{
    return trend(data, stop.minus(length), stop)
}

fun <T: Number> trend(data: Collection<DataPoint<T>>, start: Instant, stop: Instant = Instant.now()): Trend
{
    return trendByTime(data, start, stop)
}

fun <T: Number> trend(data: Collection<DataPoint<T>>, limit: Int = data.size): Trend
{
    return trendBySize(data, limit)
}

fun <T: Number> trendByTime(data: Collection<DataPoint<T>>, start: Instant, stop: Instant = Instant.now()): Trend
{
    if(start.isAfter(stop))
        throw IllegalArgumentException("Start time cannot be after end time")
    val length = Duration.between(start, stop)
    val divider: Instant = start.plus(length.dividedBy(2L))
    val divided: Pair<List<DataPoint<T>>, List<DataPoint<T>>> = data.asSequence()
        .filter { it.timestamp.isAfter(start) }
        .filter { it.timestamp.isBefore(stop) }
		.partition { it.timestamp.isBefore(divider) }

    val before: List<T> = divided.first.map { it.value }
    val after: List<T> = divided.second.map { it.value }
    return Trend(before, after)
}

fun <T: Number> trendBySize(data: Collection<DataPoint<T>>, limit: Int = data.size): Trend
{
    val breakpoint: Int = computeTakeFromLimit(data.size, limit)
    val first: List<T> = data.sortedSubList(0 until breakpoint).map { it.value }
    val second: List<T> = data.sortedSubList(breakpoint..breakpoint*2).map { it.value }
    return Trend(first, second)
}

/**
 * This function computes how many elements that should be included in each sub list from
 * originating [Collection]. This makes sure that
 * - No values less than two are used as limit (since at least two values are needed for a trend)
 * - Limits of odd size is corrected to an even size (so each sub list has an equal amount of elements)
 * - The given amount is not larger than the entire collection
 */
fun computeTakeFromLimit(size: Int, limit: Int): Int
{
    if(limit <= 1)
        throw IllegalArgumentException("Argument limit must be a greater than one but was $limit")

    val lowerBound: Int = minOf(size, limit)
    val include: Int = lowerBound - (lowerBound % 2)
    return include/2
}