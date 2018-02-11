package com.mantono.tsa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*

data class Data(override val timestamp: Instant, override val value: Int): DataPoint<Int>

class TrendsTest
{
    private val list1: List<DataPoint<Int>> = listOf(
        Data(Instant.now().minusSeconds(30), 15),
        Data(Instant.now().minusSeconds(20), 10),
        Data(Instant.now().minusSeconds(10), 8),
        Data(Instant.now().minusSeconds(0), 6)
    )

    @Test
    fun trendTestBySum()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(35))
        assertEquals(0.56, trend.bySum())
    }

    @Test
    fun trendTestBySumWithNotAllValues1()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(25))
        assertEquals(1.4, trend.bySum()) // (8 + 6) / 10
    }

    @Test
    fun trendTestBySumWithNotAllValues2()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(30), Instant.now().minusSeconds(2L))
        assertEquals(0.32, trend.bySum()) // 8 / (10 + 15)
    }

	@Test
	fun testTrendByTime()
	{
		val data: List<DataPoint<Int>> = LinkedList<DataPoint<Int>>().apply {
			add(Data(Instant.ofEpochMilli(1L), 1))
			add(Data(Instant.ofEpochMilli(2L), 1))
			add(Data(Instant.ofEpochMilli(3L), 1))
			add(Data(Instant.ofEpochMilli(7L), 2))
			add(Data(Instant.ofEpochMilli(8L), 2))
			add(Data(Instant.ofEpochMilli(9L), 2))
		}

		val start = Instant.ofEpochMilli(0L)
		val end = Instant.ofEpochMilli(10L)
		val trend = trendByTime(data, start, end)
		assertEquals(3.0, trend.first.sum())
		assertEquals(6.0, trend.second.sum())
		assertEquals(2.0, trend.byAverage(), 0.00000001)
	}

	@Test
	fun testTrendBySize()
	{
		val data: List<DataPoint<Int>> = LinkedList<DataPoint<Int>>().apply {
			add(Data(Instant.ofEpochMilli(1L), 1))
			add(Data(Instant.ofEpochMilli(2L), 1))
			add(Data(Instant.ofEpochMilli(3L), 1))
			add(Data(Instant.ofEpochMilli(7L), 2))
			add(Data(Instant.ofEpochMilli(8L), 2))
			add(Data(Instant.ofEpochMilli(9L), 2))
		}

		val trend = trendBySize(data, 6)
		assertEquals(3.0, trend.first.sum())
		assertEquals(6.0, trend.second.sum())
		assertEquals(2.0, trend.byAverage(), 0.00000001)
	}

	@Test
	fun testTrendWithOnlyDataPointInSecondHalfOfTimeSpan()
	{
		val data: List<DataPoint<Int>> = listOf(
			Data(secondsAgo(5), 1),
			Data(secondsAgo(3), 2)
		)

		val trend = trendByTime(data, Instant.now().minusSeconds(60), Instant.now())
		// Trend: 2.0 = 2 / 1
		assertEquals(2.0, trend.bySum())

	}

	private fun secondsAgo(seconds: Long): Instant = Instant.now().minusSeconds(seconds)
}