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
    val divided: Map<Boolean, List<DataPoint<T>>> = data.asSequence()
        .filter { it.timestamp.isAfter(start) }
        .filter { it.timestamp.isBefore(stop) }
        .groupBy { it.timestamp.isBefore(divider) }

    val before: List<T> = divided[true]?.map { it.value } ?: emptyList()
    val after: List<T> = divided[false]?.map { it.value } ?: emptyList()
    return Trend(before, after)
}

fun <T: Number> trendBySize(data: Collection<DataPoint<T>>, limit: Int = data.size): Trend
{
    val take: Int = computeTakeFromLimit(data.size, limit)
    val first: List<T> = dropAndTake(data, 0, take)
    val second: List<T> = dropAndTake(data ,take, take)
    return Trend(first, second)
}

fun <T: Number> dropAndTake(data: Collection<DataPoint<T>>, drop: Int, take: Int): List<T>
{
    return data.asSequence()
        .sortedBy { it.timestamp }
        .drop(drop)
        .take(take)
        .map { it.value }
        .toList()
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